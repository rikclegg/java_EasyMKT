package com.bloomberg.mktdata.samples;

import com.bloomberglp.blpapi.Message;

interface MessageHandler {
	void handleMessage(Message message);
}
