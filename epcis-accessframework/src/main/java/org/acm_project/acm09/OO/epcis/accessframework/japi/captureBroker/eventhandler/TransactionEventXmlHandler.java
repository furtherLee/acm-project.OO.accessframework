package org.acm_project.acm09.OO.epcis.accessframework.japi.captureBroker.eventhandler;

import org.acm_project.acm09.OO.epcis.accessframework.japi.captureBroker.CaptureBrokerHelper;
import org.fosstrak.epcis.model.EPCISEventType;
import org.fosstrak.epcis.model.TransactionEventType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TransactionEventXmlHandler extends EventXmlHandler{

	private static final String EventName = "TransactionEvent";
	
	public TransactionEventXmlHandler(){
		this.targetClass = TransactionEventType.class;
	}
	
	@Override
	boolean isEmptyEvent(EPCISEventType event) {
		
		boolean ret = true;
		TransactionEventType transEvent = (TransactionEventType)event;
		
		ret &= isValid(transEvent.getBizTransactionList());
		ret &= isValid(transEvent.getParentID());
		ret &= isValid(transEvent.getEpcList());
		ret &= isValid(transEvent.getAction());
		ret &= isValid(transEvent.getBizStep());
		ret &= isValid(transEvent.getDisposition());
		ret &= isValid(transEvent.getReadPoint());
		ret &= isValid(transEvent.getBizLocation());
		
		return ret;
	}

	@Override
	boolean addEventItem(Document document, Element root, EPCISEventType event) {
		boolean ret = true;
		
		TransactionEventType transEvent = (TransactionEventType)event;
		
		Element element = document.createElement(EventName);
        root.appendChild(element);
		
        ret &= CaptureBrokerHelper.addBizTransactions(document, root, transEvent.getBizTransactionList());
        ret &= CaptureBrokerHelper.addParentId(document, root, transEvent.getParentID());
        ret &= CaptureBrokerHelper.addEpcList(document, root, transEvent.getEpcList());
        ret &= CaptureBrokerHelper.addAction(document, element, transEvent.getAction().name());
        ret &= CaptureBrokerHelper.addBizStep(document, element, transEvent.getBizStep());
        ret &= CaptureBrokerHelper.addDisposition(document, element, transEvent.getDisposition());
        ret &= CaptureBrokerHelper.addReadPoint(document, element, transEvent.getReadPoint().getId());
        ret &= CaptureBrokerHelper.addBizLocation(document, element, transEvent.getBizLocation().getId());

        return ret;
	}

}
