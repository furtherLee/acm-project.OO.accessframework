package org.acm_project.acm09.OO.epcis.accessframework.soap;

import javax.xml.ws.WebFault;

/**
 * This class was generated by Apache CXF 2.2.9 Tue Jul 06 14:13:27 CEST 2010
 * Generated source version: 2.2.9
 */

@WebFault(name = "SecurityException", targetNamespace = "urn:epcglobal:epcis-query:xsd:1")
public class SecurityExceptionResponse extends Exception {
    public static final long serialVersionUID = 20100706141327L;

    private org.fosstrak.epcis.model.SecurityException securityException;

    public SecurityExceptionResponse() {
        super();
    }

    public SecurityExceptionResponse(String message) {
        super(message);
    }

    public SecurityExceptionResponse(String message, Throwable cause) {
        super(message, cause);
    }

    public SecurityExceptionResponse(String message, org.fosstrak.epcis.model.SecurityException securityException) {
        super(message);
        this.securityException = securityException;
    }

    public SecurityExceptionResponse(String message, org.fosstrak.epcis.model.SecurityException securityException,
            Throwable cause) {
        super(message, cause);
        this.securityException = securityException;
    }

    public org.fosstrak.epcis.model.SecurityException getFaultInfo() {
        return this.securityException;
    }
}
