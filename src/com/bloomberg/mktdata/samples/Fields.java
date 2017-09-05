package com.bloomberg.mktdata.samples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.bloomberg.mktdata.samples.Log.LogLevels;
import com.bloomberglp.blpapi.Element;
import com.bloomberglp.blpapi.Message;

public class Fields implements Iterable<Field> {
	
	private ArrayList<Field> fields = new ArrayList<Field>();
	
	Security security;
	ArrayList<FieldChange> fieldChanges;
	
	Fields(Security security) {
		this.security = security;
		loadFields();
	}
	
	private void loadFields() {
		for(SubscriptionField sf: security.getSecurities().easyMKT.fields) {
			Field newField = new Field(this,sf.getName(),"");
			this.fields.add(newField);
		}
	}
	
	void populateFields(Message message) {

		Log.LogMessage(LogLevels.BASIC, "Populate fields");
		Log.LogMessage(LogLevels.BASIC, "Source: " + message.toString());
		
		CurrentToOldValues();
		
		int fieldCount = message.numElements();
		
		Element e = message.asElement();
		
		for(int i=0; i<fieldCount; i++) {

			Element f = e.getElement(i);
			
			String fieldName = f.name().toString();
			
			Field fd = field(fieldName);
				
			if(fd==null) fd = new Field(this);
				
			fd.setName(fieldName);
			
			if(!f.isNull()) fd.setCurrentValue(f.getValueAsString()); 
			else fd.setCurrentValue("");
			
			Log.LogMessage(LogLevels.DETAILED, "Setting field: " + fd.name() + "\tvalue: " + fd.value());
		}
		
		fieldChanges = new ArrayList<FieldChange>();

		for(Field f: fields) {
		
			FieldChange fc = f.getFieldChanged();
			
			if(fc!=null) {
				fieldChanges.add(fc);
				f.sendNotifications(new ArrayList<FieldChange>(Arrays.asList(fc)));
			}
		}
	}
	
	ArrayList<FieldChange> getFieldChanges() {
		return this.fieldChanges;
	}
	
	void CurrentToOldValues() {
	
		for(Field f: fields) {
			f.CurrentToOld();
		}
	}
	
	public Field field(String name) {
		for(Field f: fields) {
			if(f.name().equals(name)) {
				return f;
			}
		}
		return null;
	}

	@Override
	public Iterator<Field> iterator() {
		return fields.iterator();
	}

}
