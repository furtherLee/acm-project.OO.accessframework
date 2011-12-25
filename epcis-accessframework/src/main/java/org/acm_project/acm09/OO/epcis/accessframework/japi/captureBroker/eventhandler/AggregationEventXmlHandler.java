package org.acm_project.acm09.OO.epcis.accessframework.japi.captureBroker.eventhandler;

import org.acm_project.acm09.OO.epcis.accessframework.japi.captureBroker.CaptureBrokerHelper;
import org.fosstrak.epcis.model.ActionType;
import org.fosstrak.epcis.model.EPCISEventType;
import org.fosstrak.epcis.model.AggregationEventType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AggregationEventXmlHandler extends EventXmlHandler{

	private static final String EventName = "AggregationEvent";
	
	public AggregationEventXmlHandler(){
		this.targetClass = AggregationEventType.class;
	}
	
	@Override
	boolean isEmptyEvent(EPCISEventType event) {
		
		boolean ret = true;
		AggregationEventType transEvent = (AggregationEventType)event;
		
		ret &= isValid(transEvent.getParentID());
		ret &= isValid(transEvent.getChildEPCs());
		ret &= isValid(transEvent.getAction());
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
		
		AggregationEventType transEvent = (AggregationEventType)event;
		
		Element element = document.createElement(EventName);
        root.appendChild(element);
		
		ret &= CaptureBrokerHelper.addParentId(document, root, transEvent.getParentID()) && !transEvent.getAction().equals(ActionType.OBSERVE);
        ret &= CaptureBrokerHelper.addChildEpcList(document, element, transEvent.getChildEPCs()); 
        ret &= CaptureBrokerHelper.addAction(document, element, transEvent.getAction().name());
        ret &= CaptureBrokerHelper.addBizStep(document, element, transEvent.getBizStep());
        ret &= CaptureBrokerHelper.addDisposition(document, element, transEvent.getDisposition());
        ret &= CaptureBrokerHelper.addReadPoint(document, element, transEvent.getReadPoint().getId());
        ret &= CaptureBrokerHelper.addBizLocation(document, element, transEvent.getBizLocation().getId());
        ret &= CaptureBrokerHelper.addBizTransactions(document, element, transEvent.getBizTransactionList());
        
        return ret;
	}


	
}
