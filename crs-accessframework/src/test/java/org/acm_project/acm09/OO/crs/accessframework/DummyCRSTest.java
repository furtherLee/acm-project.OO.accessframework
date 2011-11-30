package org.acm_project.acm09.OO.crs.accessframework;

import java.util.HashMap;

import org.acm_project.acm09.OO.crs.accessframework.webadapter.CRSWebAdapter;

public class DummyCRSTest implements CRSResolvable{
	
	private static HashMap<String, String> epcMap = new HashMap<String, String>();
	
	static{
		epcMap.put("urn:epc:id:sgtin:0614141.000024.400", "http://localhost:8080/epcis-repository/");
		epcMap.put("urn:epc:id:sgtin:0614141.000025.400", "http://localhost:8080/epcis-repository/");
		epcMap.put("urn:epc:id:sgtin:0614141.000026.400", "http://localhost:8080/epcis-repository/");
		epcMap.put("urn:epc:id:sgtin:0614141.000027.400", "http://localhost:8080/epcis-repository/");
		epcMap.put("urn:epc:id:sgtin:0614141.000028.400", "http://localhost:8080/epcis-repository/");
	}
	
	public DummyCRSTest(){}
	@Override
	public String getEpcisLocation(String epc) {
		if (!isValid(epc))
			return "Not Found";
		
		String ret = epcMap.get(epc.trim());
		if (ret == null)
			return "Not Found";
		return ret;
	}

	private boolean isValid(String epc){
		if (epc == null)
			return false;
		return epc.trim().startsWith("urn:epc:");
	}
	
	public static void main(String[] args){
		CRSWebAdapter adp = new CRSWebAdapter("http://localhost:9998/", new DummyCRSTest());
		adp.start();
	}
	
}
