package org.acm_project.acm09.OO.epcis.accessframework.japi.captureBroker.eventhandler;

import org.acm_project.acm09.OO.epcis.accessframework.japi.captureBroker.CaptureBrokerHelper;
import org.fosstrak.epcis.model.EPCISEventType;
import org.fosstrak.epcis.model.ObjectEventType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ObjectEventXmlHandler extends EventXmlHandler{

	private static final String EventName = "ObjectEvent";
	
	public ObjectEventXmlHandler(){
		this.targetClass = ObjectEventType.class;
	}
	
	@Override
	protected boolean isEmptyEvent(EPCISEventType event) {

		ObjectEventType transEvent = (ObjectEventType)event;
		
		boolean ret = true;
		
		ret &= isValid(transEvent.getEventTime());
		ret &= isValid(transEvent.getEventTimeZoneOffset());
		ret &= isValid(transEvent.getEpcList());
		ret &= isValid(transEvent.getAction());
		ret &= isValid(transEvent.getBizStep());
		ret &= isValid(transEvent.getDisposition());
		ret &= isValid(transEvent.getReadPoint());
		ret &= isValid(transEvent.getBizLocation());
		ret &= isValid(transEvent.getBizTransactionList());
		
		return ret;
	}

	@Override
	protected boolean addEventItem(Document document, Element root, EPCISEventType event) {
		boolean ret = true;
		
		ObjectEventType transEvent = (ObjectEventType)event;
		
		Element element = document.createElement(EventName);
        root.appendChild(element);
        
        ret &= CaptureBrokerHelper.addEventTime(document, element, transEvent.getEventTime().toString());
        ret &= CaptureBrokerHelper.addEventTimeZoneOffset(document, element, transEvent.getEventTimeZoneOffset());
        ret &= CaptureBrokerHelper.addEpcList(document, element, transEvent.getEpcList());
        ret &= CaptureBrokerHelper.addAction(document, element, transEvent.getAction().name());
        ret &= CaptureBrokerHelper.addBizStep(document, element, transEvent.getBizStep());
        ret &= CaptureBrokerHelper.addDisposition(document, element, transEvent.getDisposition());
        ret &= CaptureBrokerHelper.addReadPoint(document, element, transEvent.getReadPoint().getId());
        ret &= CaptureBrokerHelper.addBizLocation(document, element, transEvent.getBizLocation().getId());
        ret &= CaptureBrokerHelper.addBizTransactions(document, element, transEvent.getBizTransactionList());
        
        return ret;
	}


}
