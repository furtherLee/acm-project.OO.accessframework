package org.acm_project.acm09.OO.crs.accessframework;

import org.acm_project.acm09.OO.crs.accessframework.japi.CRSBroker;
import org.acm_project.acm09.OO.crs.accessframework.japi.CacheCRSBroker;

public class DemoTest1 {

	public static void main(String[] args){
        CRSBroker broker = new CacheCRSBroker(args[0].trim(), Integer.parseInt(args[1].trim()));
        System.out.println(broker.getEpcisLocation("urn:epc:id:sgtin:0614141.000024.400"));
        System.out.println(broker.getEpcisLocation("urn:epc:id:sgtin:0614141.000026.400"));
        System.out.println(broker.getEpcisLocation("urn:epc:id:sgtin:0614141.000026.41"));
        System.out.println(broker.getEpcisLocation("urn::epc:id:sgtin:0614141.000026.400"));
        System.out.println(broker.getEpcisLocation("urn:epc:id:sgtin:0614141.000024.400"));
	}
	
}
