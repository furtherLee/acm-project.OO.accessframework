package org.acm_project.acm09.OO.epcis.accessframework.japi.captureBroker.eventhandler;

import org.acm_project.acm09.OO.epcis.accessframework.japi.captureBroker.CaptureBrokerHelper;
import org.fosstrak.epcis.model.EPCISEventType;
import org.fosstrak.epcis.model.QuantityEventType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class QuantityEventXmlHandler extends EventXmlHandler{

	private static final String EventName = "QuantityEvent";
	
	public QuantityEventXmlHandler(){
		this.targetClass = QuantityEventType.class;
	}
	
	@Override
	boolean isEmptyEvent(EPCISEventType event) {
		boolean ret = true;
		
		QuantityEventType transEvent = (QuantityEventType)event;
		
		ret &= isValid(transEvent.getEpcClass());
		ret &= isValid(transEvent.getQuantity());
		ret &= isValid(transEvent.getBizStep());
		ret &= isValid(transEvent.getDisposition());
		ret &= isValid(transEvent.getReadPoint());
		ret &= isValid(transEvent.getBizLocation());
		ret &= isValid(transEvent.getBizTransactionList());
		
		return ret;
	}

	@Override
	boolean addEventItem(Document document, Element root, EPCISEventType event) {
		boolean ret = true;
		
		QuantityEventType transEvent = (QuantityEventType)event;
		
		Element element = document.createElement(EventName);
        root.appendChild(element);
		
        ret &= CaptureBrokerHelper.addEpcClass(document, element, transEvent.getEpcClass());
        ret &= CaptureBrokerHelper.addQuantity(document, element, new Integer(transEvent.getQuantity()).toString());
        ret &= CaptureBrokerHelper.addBizStep(document, element, transEvent.getBizStep());
        ret &= CaptureBrokerHelper.addDisposition(document, element, transEvent.getDisposition());
        ret &= CaptureBrokerHelper.addReadPoint(document, element, transEvent.getReadPoint().getId());
        ret &= CaptureBrokerHelper.addBizLocation(document, element, transEvent.getBizLocation().getId());
        ret &= CaptureBrokerHelper.addBizTransactions(document, element, transEvent.getBizTransactionList());
	
        return ret;
	}

}
