package org.acm_project.acm09.OO.epcis.accessframework.japi.captureBroker.eventhandler;

import org.fosstrak.epcis.model.EPCISEventType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class EventXmlHandler {

	protected Class<?> targetClass;
	
	public boolean addEvent(final Document document,
			final Element root, final EPCISEventType event){
		
		if (!isInstance(event)) return false;
		if (isEmptyEvent(event)) return false;
		return addEventItem(document, root, event);
	
	}

	protected boolean isInstance(EPCISEventType event) {
		return targetClass.isInstance(event);
	}

	abstract boolean isEmptyEvent(EPCISEventType event);

	abstract boolean addEventItem(final Document document, final Element root,
			final EPCISEventType event);
	
	protected boolean isValid(Object o){
		if (o == null)
			return false;
		
		if (o instanceof String)
			return o.equals("");
		
		return true;
	}
}
