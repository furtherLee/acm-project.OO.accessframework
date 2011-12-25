package org.acm_project.acm09.OO.epcis.accessframework;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.acm_project.acm09.OO.epcis.accessframework.japi.captureBroker.CaptureBroker;
import org.acm_project.acm09.OO.epcis.accessframework.japi.captureBroker.eventhandler.EventXmlHandlerFactory;
import org.fosstrak.epcis.model.ActionType;
import org.fosstrak.epcis.model.BusinessLocationType;
import org.fosstrak.epcis.model.BusinessTransactionListType;
import org.fosstrak.epcis.model.BusinessTransactionType;
import org.fosstrak.epcis.model.EPC;
import org.fosstrak.epcis.model.EPCListType;
import org.fosstrak.epcis.model.ObjectEventType;
import org.fosstrak.epcis.model.ReadPointType;

public class SimpleTest {
	
	public static void main(String[] args) throws DatatypeConfigurationException{
		CaptureBroker broker = new CaptureBroker("http://localhost:8080/epcis-repository/capture");
		ObjectEventType event = new ObjectEventType();
		ReadPointType readpoint = new ReadPointType();
		readpoint.setId("eee readpoint");
		XMLGregorianCalendar calendar =  DatatypeFactory.newInstance().newXMLGregorianCalendar(2011, 12, 24, 23, 42, 31, 0, 8);
		EPCListType list = new EPCListType();
		EPC epc = new EPC();
		epc.setValue("urn:epc:id:sgtin:11111.11111.00002");
		list.getEpc().add(epc);
		BusinessLocationType location = new BusinessLocationType();
		location.setId("urn:epc:id:global:D19-308");
		BusinessTransactionType transaction = new BusinessTransactionType();
		transaction.setType("肾");
		transaction.setValue("第一个肾");
		BusinessTransactionListType bizList = new BusinessTransactionListType();
		bizList.getBizTransaction().add(transaction);
		
		event.setBizStep("摘肾");
		event.setAction(ActionType.OBSERVE);
		event.setEventTime(calendar);
		event.setDisposition("eee disposition");
		event.setReadPoint(readpoint);
		event.setEpcList(list);
		event.setBizLocation(location);
		event.setBizTransactionList(bizList);
		event.setEventTimeZoneOffset("+08:00");
		
		if(broker.captureWithCheck(event))
			System.out.println("OK");
		else
			System.out.println("WRONG");
	}
	
}
