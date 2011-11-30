package org.acm_project.acm09.OO.crs.accessframework;

import org.acm_project.acm09.OO.crs.accessframework.webadapter.CRSWebAdapter;

public class DummyCRSTest implements CRSResolvable{

	
	public DummyCRSTest(){}
	@Override
	public String getEpcisLocation(String epc) {
		return "uncle fucker";
	}

	public static void main(String[] args){
		CRSWebAdapter adp = new CRSWebAdapter("http://localhost:9998/", new DummyCRSTest());
		adp.start();
	}
	
}
