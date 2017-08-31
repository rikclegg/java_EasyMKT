package com.bloomberg.mktdata.samples;

import java.util.ArrayList;
import java.util.Iterator;

import com.bloomberg.mktdata.samples.Log.LogLevels;
import com.bloomberglp.blpapi.Element;
import com.bloomberglp.blpapi.Message;

public class Fields implements Iterable<Field> {
	
	private ArrayList<Field> fields = new ArrayList<Field>();
	
	Security security;
	
	private ArrayList<FieldChange> fieldChanges;
	
	Fields(Security security) {
		this.security = security;
	}
	
	void populateFields(Message message) {

		Log.LogMessage(LogLevels.BASIC, "Populate fields");
		
		CurrentToOldValues();
		
		int fieldCount = message.numElements();
		
		Element e = message.asElement();
		
		fieldChanges = new ArrayList<FieldChange>();
		
		for(int i=0; i<fieldCount; i++) {

			Element f = e.getElement(i);
			
			String fieldName = f.name().toString();
			
			Field fd = field(fieldName);
				
			if(fd==null) fd = new Field(this);
				
			fd.setName(fieldName);
			fd.setCurrentValue(f.getValueAsString()); 
			
			FieldChange fc = fd.getFieldChanged();
			if(fc!=null) {
				fieldChanges.add(fc);
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
