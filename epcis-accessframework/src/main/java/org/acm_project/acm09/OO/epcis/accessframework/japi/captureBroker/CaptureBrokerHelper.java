package org.acm_project.acm09.OO.epcis.accessframework.japi.captureBroker;

import java.io.StringWriter;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.fosstrak.epcis.captureclient.CaptureClientHelper;
import org.fosstrak.epcis.model.ActionType;
import org.fosstrak.epcis.model.AggregationEventType;
import org.fosstrak.epcis.model.BusinessTransactionListType;
import org.fosstrak.epcis.model.BusinessTransactionType;
import org.fosstrak.epcis.model.EPC;
import org.fosstrak.epcis.model.EPCISEventType;
import org.fosstrak.epcis.model.EPCListType;
import org.fosstrak.epcis.model.ObjectEventType;
import org.fosstrak.epcis.model.QuantityEventType;
import org.fosstrak.epcis.model.TransactionEventType;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CaptureBrokerHelper extends CaptureClientHelper {

	private static Document creatDocument() {
		Document document = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation impl = builder.getDOMImplementation();
			document = impl.createDocument("urn:epcglobal:epcis:xsd:1",
					"epcis:EPCISDocument", null);
		} catch (ParserConfigurationException exp) {
			exp.printStackTrace();
		}
		return document;
	}

	private static Element initDocument(Document document) {

		Element element = null;

		Element root = document.getDocumentElement();

		root.setAttribute("creationDate",
				CaptureBrokerHelper.format(Calendar.getInstance()));
		root.setAttribute("xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		root.setAttribute("xmlns:epcis", "urn:epcglobal:epcis:xsd:1");
		root.setAttribute("schemaVersion", "1.0");
		element = document.createElement("EPCISBody");
		root.appendChild(element);
		root = element;
		element = document.createElement("EventList");
		root.appendChild(element);
		root = element;

		return element;
	}

	public static String makeEvent(EPCISEventType event) {
		Document document = creatDocument();

		if (document == null)
			return null;

		Element root = initDocument(document);
		if(!addEvent(document, root, event))
			return null;
		
		return documentToString(document);
	}

	public static String makeEvents(List<EPCISEventType> events) {
		Document document = creatDocument();

		if (document == null)
			return null;

		Element root = initDocument(document);

		for (EPCISEventType event : events)
			if(!addEvent(document, root, event))
				return null;
		
		return documentToString(document);
	}

	private static String documentToString(Document document){
        DOMSource domsrc = new DOMSource(document);
    	
        StringWriter out = new StringWriter();
        StreamResult streamResult = new StreamResult(out);
        
		try{
	        TransformerFactory tf = TransformerFactory.newInstance();
	        Transformer serializer = tf.newTransformer();
	
	        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
	        serializer.transform(domsrc, streamResult);
		}
		catch(Exception exp){
			exp.printStackTrace();
			return null;
		}
		
        String eventXml = out.toString();

        return eventXml;
	}
	
	private static boolean addEvent(final Document document,
			final Element root, EPCISEventType event) {
		if (event instanceof ObjectEventType){
			return addEvent(document, root, (ObjectEventType)event);
		}
		else if(event instanceof AggregationEventType){
			return addEvent(document, root, (AggregationEventType)event);
		}
		else if(event instanceof QuantityEventType){
			return addEvent(document, root, (QuantityEventType)event);
		}
		else if(event instanceof TransactionEventType){
			return addEvent(document, root, (TransactionEventType)event);
		}
		
		return false;
	}

	private static boolean addEvent(final Document document,
			final Element root, AggregationEventType event) {
        
		boolean ret = true;
		
		Element element = document.createElement(EpcisEventType.ObjectEvent.name());
        root.appendChild(element);
		
		ret &= addParentId(document, root, event.getParentID()) && !event.getAction().equals(ActionType.OBSERVE);
        ret &= CaptureBrokerHelper.addChildEpcList(document, element, event.getChildEPCs()); 
        ret &= CaptureBrokerHelper.addAction(document, element, event.getAction().name());
        ret &= CaptureBrokerHelper.addBizStep(document, element, event.getBizStep());
        ret &= CaptureBrokerHelper.addDisposition(document, element, event.getDisposition());
        ret &= CaptureBrokerHelper.addReadPoint(document, element, event.getReadPoint().getId());
        ret &= CaptureBrokerHelper.addBizLocation(document, element, event.getBizLocation().getId());
        ret &= CaptureBrokerHelper.addBizTransactions(document, element, event.getBizTransactionList());
        
        return ret;
	}

	private static boolean addChildEpcList(Document document, Element element,
			EPCListType childEPCs) {

		List<EPC> list = childEPCs.getEpc();
		
		if (list.isEmpty()) {
            return false;
        }
		
        Element tempElement = document.createElement("childEPCs");
        for(EPC epc: list)
        	addElement(document, tempElement, epc.getValue(), "epc");
        element.appendChild(tempElement);
        
        return true;
	}

	private static boolean addEvent(final Document document,
			final Element root, ObjectEventType event) {

		boolean ret = true;
		
		Element element = document.createElement(EpcisEventType.ObjectEvent.name());
        root.appendChild(element);
        
        ret &= addEventTime(document, element, event.getEventTime().toString());
        ret &= addEventTimeZoneOffset(document, element, event.getEventTimeZoneOffset());
        ret &= addEpcList(document, element, event.getEpcList());
        ret &= addAction(document, element, event.getAction().name());
        ret &= addBizStep(document, element, event.getBizStep());
        ret &= addDisposition(document, element, event.getDisposition());
        ret &= addReadPoint(document, element, event.getReadPoint().getId());
        ret &= addBizLocation(document, element, event.getBizLocation().getId());
        ret &= addBizTransactions(document, element, event.getBizTransactionList());
        return ret;
	}

	private static boolean addEpcList(Document document, Element element,
			EPCListType epcList) {
        
		List<EPC> list = epcList.getEpc();
		
		if (list.isEmpty()) {
            return false;
        }
		
        Element tempElement = document.createElement("epcList");
        for (EPC epc: list)
            addElement(document, tempElement, epc.getValue(), "epc");
        element.appendChild(tempElement);
        
        return true;
	}

	private static boolean addBizTransactions(Document document,
			Element element, BusinessTransactionListType bizTransactionList) {

		List<BusinessTransactionType> list = bizTransactionList.getBizTransaction();
		
		if (list.isEmpty())
			return false;
		
        Element tempElement = document.createElement("bizTransactionList");
        
        for (BusinessTransactionType biz: list){
            Element bizNode = document.createElement("bizTransaction");
            bizNode.appendChild(document.createTextNode(biz.getValue()));
            bizNode.setAttribute("type", biz.getType());
            tempElement.appendChild(bizNode);
        }
        
        element.appendChild(tempElement);

        return true;
	}

	private static boolean addEvent(final Document document,
			final Element root, QuantityEventType event) {
		
		boolean ret = true;
		
		Element element = document.createElement(EpcisEventType.ObjectEvent.name());
        root.appendChild(element);
		
        ret &= CaptureBrokerHelper.addEpcClass(document, element, event.getEpcClass());
        ret &= CaptureBrokerHelper.addQuantity(document, element, new Integer(event.getQuantity()).toString());
        ret &= CaptureBrokerHelper.addBizStep(document, element, event.getBizStep());
        ret &= CaptureBrokerHelper.addDisposition(document, element, event.getDisposition());
        ret &= CaptureBrokerHelper.addReadPoint(document, element, event.getReadPoint().getId());
        ret &= CaptureBrokerHelper.addBizLocation(document, element, event.getBizLocation().getId());
        ret &= CaptureBrokerHelper.addBizTransactions(document, element, event.getBizTransactionList());
	
        return ret;
	}

	private static boolean addEvent(final Document document,
			final Element root, TransactionEventType event) {
		
		boolean ret = true;
		
		Element element = document.createElement(EpcisEventType.ObjectEvent.name());
        root.appendChild(element);
		
        ret &= CaptureBrokerHelper.addBizTransactions(document, root, event.getBizTransactionList());
        ret &= CaptureBrokerHelper.addParentId(document, root, event.getParentID());
        ret &= CaptureBrokerHelper.addEpcList(document, root, event.getEpcList());
        ret &= CaptureBrokerHelper.addAction(document, element, event.getAction().name());
        ret &= CaptureBrokerHelper.addBizStep(document, element, event.getBizStep());
        ret &= CaptureBrokerHelper.addDisposition(document, element, event.getDisposition());
        ret &= CaptureBrokerHelper.addReadPoint(document, element, event.getReadPoint().getId());
        ret &= CaptureBrokerHelper.addBizLocation(document, element, event.getBizLocation().getId());

        return ret;
	}
}
