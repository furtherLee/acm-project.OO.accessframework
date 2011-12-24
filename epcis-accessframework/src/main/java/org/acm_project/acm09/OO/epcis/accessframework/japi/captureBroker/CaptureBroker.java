package org.acm_project.acm09.OO.epcis.accessframework.japi.captureBroker;

import java.io.InputStream;
import java.util.List;

import org.acm_project.acm09.OO.epcis.accessframework.japi.Broker;
import org.acm_project.acm09.OO.epcis.accessframework.japi.CaptureInterface;
import org.fosstrak.epcis.captureclient.CaptureClient;
import org.fosstrak.epcis.captureclient.CaptureClientException;
import org.fosstrak.epcis.model.Document;
import org.fosstrak.epcis.model.EPCISEventType;

public class CaptureBroker extends Broker implements CaptureInterface{

	CaptureClient client;

	
	public CaptureBroker(){
		client = new CaptureClient();
	}
	
	public CaptureBroker(String url){
		super.setUrl(url);
		client = new CaptureClient(url);
	}
	
	public CaptureBroker(final String url, Object[] authOptions){
		super.setUrl(url);
		client = new CaptureClient(url, authOptions);
	}
	
    public int capture(final InputStream xmlStream) throws CaptureClientException {
        return client.capture(xmlStream);
    }
	
    public int capture(final String eventXml) throws CaptureClientException {
    	return client.capture(eventXml);
    }
    
    public int capture(final Document epcisDoc) throws CaptureClientException{
    	return client.capture(epcisDoc);
    }
	
	@Override
	public int testLink() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void capture(List<EPCISEventType> events) {
		captureWithCheck(events);
	}
	
	public void capture(EPCISEventType event){
		captureWithCheck(event);
	}

	public boolean captureWithCheck(List<EPCISEventType> events){
		String xmlString = CaptureBrokerHelper.makeEvents(events);
		boolean ret = (xmlString != null);
		try{
			if (ret)
				ret = (capture(xmlString) == 200);
		}
		catch(CaptureClientException exp){
		}
		
		return ret;
	}
	
	public boolean captureWithCheck(EPCISEventType event){
		String xmlString = CaptureBrokerHelper.makeEvent(event);
		boolean ret = (xmlString != null);
		try{
			if (ret)
				ret = (capture(xmlString) == 200);
		}
		catch(CaptureClientException exp){
		}
		return ret;
	}
	
}
