package com.bloomberg.mktdata.samples;

import java.util.ArrayList;
import java.util.Iterator;

public class Securities implements Iterable<Security> {

	EasyMKT easyMKT;
	private ArrayList<Security> securities = new ArrayList<Security>();

	Securities(EasyMKT easyMKT) {
		this.easyMKT = easyMKT;
		updateSubscriptions();
	}
	
	private void updateSubscriptions() {
		// When a security is added, we create a new subscription for the current list of Subscription Fields.
	}

	@Override
	public Iterator<Security> iterator() {
		return securities.iterator();
	}

	public Security get(int index) {
		return securities.get(index);
	}
	
	public Security get(String name) {
		for(Security s: securities) {
			if(s.getName().equals(name)) return s;
		}
		return null;
	}

	Security createSecurity(String ticker) {
		Security newSecurity = new Security(this,ticker);
		securities.add(newSecurity);
		return newSecurity;
	}

}
