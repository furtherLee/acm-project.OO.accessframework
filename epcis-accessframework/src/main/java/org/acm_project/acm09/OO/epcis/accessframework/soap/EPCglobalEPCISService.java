package org.acm_project.acm09.OO.epcis.accessframework.soap;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.2.9 Tue Jul 06 14:13:27 CEST 2010
 * Generated source version: 2.2.9
 */

@WebServiceClient(name = "EPCglobalEPCISService", wsdlLocation = "file:/Z:/Documents%20and%20Settings/tzhstmat/workspaceEPCIS/epcis/epcis-commons/../epcis-repository/src/main/resources/wsdl/EPCglobal-epcis-query-1_0.wsdl", targetNamespace = "urn:epcglobal:epcis:wsdl:1")
public class EPCglobalEPCISService extends Service {

    public final static URL WSDL_LOCATION;
    public final static QName SERVICE = new QName("urn:epcglobal:epcis:wsdl:1", "EPCglobalEPCISService");
    public final static QName EPCglobalEPCISServicePort = new QName("urn:epcglobal:epcis:wsdl:1",
            "EPCglobalEPCISServicePort");

    static {
        URL url = null;
        try {
            url = new URL("http://demo.fosstrak.org/epcis/query?wsdl");
        } catch (MalformedURLException e) {
            System.err.println("Can not initialize the default wsdl from http://demo.fosstrak.org/epcis/query?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public EPCglobalEPCISService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public EPCglobalEPCISService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public EPCglobalEPCISService() {
        super(WSDL_LOCATION, SERVICE);
    }

    /**
     * @return returns EPCISServicePortType
     */
    @WebEndpoint(name = "EPCglobalEPCISServicePort")
    public EPCISServicePortType getEPCglobalEPCISServicePort() {
        return super.getPort(EPCglobalEPCISServicePort, EPCISServicePortType.class);
    }

    /**
     * @param features
     *            A list of {@link javax.xml.ws.WebServiceFeature} to configure
     *            on the proxy. Supported features not in the
     *            <code>features</code> parameter will have their default
     *            values.
     * @return returns EPCISServicePortType
     */
    @WebEndpoint(name = "EPCglobalEPCISServicePort")
    public EPCISServicePortType getEPCglobalEPCISServicePort(WebServiceFeature... features) {
        return super.getPort(EPCglobalEPCISServicePort, EPCISServicePortType.class, features);
    }

}
