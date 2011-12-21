package org.acm_project.acm09.OO.crs.accessframework;

import org.acm_project.acm09.OO.crs.accessframework.webadapter.CRSWebAdapter;

public class DemoServer {
	
	public static void main(String[] args){

		CRSWebAdapter adp = new CRSWebAdapter(args[0].trim(), new DummyCRSTest());
		adp.start();
	}
	
}
