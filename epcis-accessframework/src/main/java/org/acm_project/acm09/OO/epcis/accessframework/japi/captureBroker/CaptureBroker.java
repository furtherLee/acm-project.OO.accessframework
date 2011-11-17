package org.acm_project.acm09.OO.epcis.accessframework.japi.captureBroker;

import java.util.List;

import org.acm_project.acm09.OO.epcis.accessframework.japi.Broker;
import org.acm_project.acm09.OO.epcis.accessframework.japi.CaptureInterface;
import org.fosstrak.epcis.model.EPCISEventType;

public class CaptureBroker extends Broker implements CaptureInterface{

	
	
	
	
	
	@Override
	public int testLink() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void capture(List<EPCISEventType> events) {
		// TODO Auto-generated method stub
		
	}

}
