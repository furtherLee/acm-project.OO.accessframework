package org.acm_project.acm09.OO.crs.accessframework;

import org.acm_project.acm09.OO.crs.accessframework.japi.CRSBroker;
import org.acm_project.acm09.OO.crs.accessframework.japi.CacheCRSBroker;

import junit.framework.TestCase;

public class CRSTester extends TestCase {

    public void testCRS() {

        CRSBroker broker = new CacheCRSBroker("http://localhost", 9998);
        System.out.println(broker.getEpcisLocation("urn:epc:id:sgtin:0614141.000024.400"));
        System.out.println(broker.getEpcisLocation("urn:epc:id:sgtin:0614141.000026.400"));
        System.out.println(broker.getEpcisLocation("urn:epc:id:sgtin:0614141.000026.41"));
        System.out.println(broker.getEpcisLocation("urn::epc:id:sgtin:0614141.000026.400"));
        System.out.println(broker.getEpcisLocation("urn:epc:id:sgtin:0614141.000024.400"));

    }
}
