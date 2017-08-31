/* Copyright 2017. Bloomberg Finance L.P.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:  The above
 * copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package com.bloomberg.mktdata.samples;

import com.bloomberg.mktdata.samples.Log.LogLevels;
import com.bloomberglp.blpapi.CorrelationID;
import com.bloomberglp.blpapi.Event;
import com.bloomberglp.blpapi.EventHandler;
import com.bloomberglp.blpapi.Message;
import com.bloomberglp.blpapi.MessageIterator;
import com.bloomberglp.blpapi.Name;
import com.bloomberglp.blpapi.Service;
import com.bloomberglp.blpapi.Session;
import com.bloomberglp.blpapi.SessionOptions;

public class EasyMKT {
	
	// ADMIN
	private static final Name 	SLOW_CONSUMER_WARNING	= new Name("SlowConsumerWarning");
	private static final Name 	SLOW_CONSUMER_WARNING_CLEARED	= new Name("SlowConsumerWarningCleared");

	// SESSION_STATUS
	private static final Name 	SESSION_STARTED 		= new Name("SessionStarted");
	private static final Name 	SESSION_TERMINATED 		= new Name("SessionTerminated");
	private static final Name 	SESSION_STARTUP_FAILURE = new Name("SessionStartupFailure");
	private static final Name 	SESSION_CONNECTION_UP 	= new Name("SessionConnectionUp");
	private static final Name 	SESSION_CONNECTION_DOWN	= new Name("SessionConnectionDown");

	// SERVICE_STATUS
	private static final Name 	SERVICE_OPENED 			= new Name("ServiceOpened");
	private static final Name 	SERVICE_OPEN_FAILURE 	= new Name("ServiceOpenFailure");

	// SUBSCRIPTION_STATUS + SUBSCRIPTION_DATA
	private static final Name	SUBSCRIPTION_FAILURE 	= new Name("SubscriptionFailure");
	private static final Name	SUBSCRIPTION_STARTED	= new Name("SubscriptionStarted");
	private static final Name	SUBSCRIPTION_TERMINATED	= new Name("SubscriptionTerminated");

	public Securities securities;
	public SubscriptionFields fields;

	private String host;
	private int port;
	
	Session session;
	Service service;
	
	private boolean ready=false;
	
	private static final String MKTDATA_SERVICE = "//blp/mktdata";
	
	public EasyMKT() {
		this.host = "localhost";
		this.port = 8194;
		initialise();
	}
	
	public EasyMKT(String host, int port) {
		this.host = host;
		this.port = port;
		initialise();
	}

	private void initialise() {

		fields = new SubscriptionFields(this);
		
    	SessionOptions sessionOptions = new SessionOptions();
        sessionOptions.setServerHost(this.host);
        sessionOptions.setServerPort(this.port);

        Session session = new Session(sessionOptions, new EMSXEventHandler(this));
        
        try {
			session.startAsync();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        while(!ready);
		
	}
	
	public SubscriptionField addField(String fieldName) {
		
		return fields.createSubscriptionField(fieldName);
	}
	
	class EMSXEventHandler implements EventHandler
    {
		EasyMKT easyMKT;
		
		EMSXEventHandler(EasyMKT easyMKT) {
			this.easyMKT = easyMKT;
		}
		
        public void processEvent(Event event, Session session)
        {
            try {
                switch (event.eventType().intValue())
                {                
                case Event.EventType.Constants.ADMIN:
                    processAdminEvent(event, session);
                    break;
                case Event.EventType.Constants.SESSION_STATUS:
                    processSessionEvent(event, session);
                    break;
                case Event.EventType.Constants.SERVICE_STATUS:
                    processServiceEvent(event, session);
                    break;
                case Event.EventType.Constants.SUBSCRIPTION_DATA:
                    processSubscriptionDataEvent(event, session);
                    break;
                case Event.EventType.Constants.SUBSCRIPTION_STATUS:
                    processSubscriptionStatus(event, session);
                    break;
                default:
                    processMiscEvents(event, session);
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

		private void processAdminEvent(Event event, Session session) throws Exception 
		{
			Log.LogMessage(LogLevels.DETAILED, "Processing " + event.eventType().toString());
        	
			MessageIterator msgIter = event.messageIterator();
            
			while (msgIter.hasNext()) {
                Message msg = msgIter.next();
                if(msg.messageType().equals(SLOW_CONSUMER_WARNING)) {
                	Log.LogMessage(LogLevels.DETAILED, "Warning: Entered Slow Consumer status");
                } else if(msg.messageType().equals(SLOW_CONSUMER_WARNING_CLEARED)) {
                	Log.LogMessage(LogLevels.DETAILED, "Slow consumer status cleared");
                }
            }
		}

		private void processSessionEvent(Event event, Session session) throws Exception {

			Log.LogMessage(LogLevels.DETAILED, "Processing " + event.eventType().toString());
        	
			MessageIterator msgIter = event.messageIterator();
            
			while (msgIter.hasNext()) {
            
				Message msg = msgIter.next();
                
				if(msg.messageType().equals(SESSION_STARTED)) {
					Log.LogMessage(LogLevels.DETAILED, "Session started...");
                	session.openServiceAsync(MKTDATA_SERVICE);
				} else if(msg.messageType().equals(SESSION_STARTUP_FAILURE)) {
					Log.LogMessage(LogLevels.DETAILED, "Error: Session startup failed");
                } else if(msg.messageType().equals(SESSION_TERMINATED)) {
                	Log.LogMessage(LogLevels.DETAILED, "Session has been terminated");
                } else if(msg.messageType().equals(SESSION_CONNECTION_UP)) {
                	Log.LogMessage(LogLevels.DETAILED, "Session connection is up");
                } else if(msg.messageType().equals(SESSION_CONNECTION_DOWN)) {
                	Log.LogMessage(LogLevels.DETAILED, "Session connection is down");
                }
            }
		}

        private void processServiceEvent(Event event, Session session) {

        	Log.LogMessage(LogLevels.DETAILED, "Processing " + event.eventType().toString());
        	
        	MessageIterator msgIter = event.messageIterator();
            
        	while (msgIter.hasNext()) {
            
        		Message msg = msgIter.next();
                
        		if(msg.messageType().equals(SERVICE_OPENED)) {
                
        			Log.LogMessage(LogLevels.DETAILED, "Service opened...");
                	
                    easyMKT.service = session.getService(MKTDATA_SERVICE);
                	
                    this.easyMKT.ready=true;
                    
                } else if(msg.messageType().equals(SERVICE_OPEN_FAILURE)) {
                	Log.LogMessage(LogLevels.DETAILED, "Error: Service failed to open");
                }
            }
		}

		private void processSubscriptionStatus(Event event, Session session) throws Exception 
		{
			Log.LogMessage(LogLevels.DETAILED, "Processing " + event.eventType().toString());
            
			MessageIterator msgIter = event.messageIterator();
            
			while (msgIter.hasNext()) {
                Message msg = msgIter.next();
                if(msg.messageType().equals(SUBSCRIPTION_STARTED)) {
                	Log.LogMessage(LogLevels.DETAILED, "Subscription started successfully: " + msg.correlationID().toString());
                } else if(msg.messageType().equals(SUBSCRIPTION_FAILURE)) {
                	Log.LogMessage(LogLevels.DETAILED, "Error: Subscription failed: "+ msg.correlationID().toString());
                } else if(msg.messageType().equals(SUBSCRIPTION_TERMINATED)) {
                	Log.LogMessage(LogLevels.DETAILED, "Subscription terminated : " + msg.correlationID().toString());
                }
            }
        }

        private void processSubscriptionDataEvent(Event event, Session session) throws Exception
        {

			Log.LogMessage(LogLevels.DETAILED, "Processing " + event.eventType().toString());

			MessageIterator msgIter = event.messageIterator();
            
        	while (msgIter.hasNext()) {
            
        		Message msg = msgIter.next();

        		// process the incoming market data event
            }
        }

        private void processMiscEvents(Event event, Session session) throws Exception 
        {
			Log.LogMessage(LogLevels.DETAILED, "Processing " + event.eventType().toString());

			MessageIterator msgIter = event.messageIterator();
            
			while (msgIter.hasNext()) {
                Message msg = msgIter.next();
                Log.LogMessage(LogLevels.DETAILED, "MESSAGE: " + msg);
            }
        }

    }	

}
