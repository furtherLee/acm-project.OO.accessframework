package org.acm_project.acm09.OO.epcis.accessframework.soap;

import javax.xml.ws.WebFault;

/**
 * This class was generated by Apache CXF 2.2.9 Tue Jul 06 14:13:27 CEST 2010
 * Generated source version: 2.2.9
 */

@WebFault(name = "ImplementationException", targetNamespace = "urn:epcglobal:epcis-query:xsd:1")
public class ImplementationExceptionResponse extends Exception {
    public static final long serialVersionUID = 20100706141327L;

    private org.fosstrak.epcis.model.ImplementationException implementationException;

    public ImplementationExceptionResponse() {
        super();
    }

    public ImplementationExceptionResponse(String message) {
        super(message);
    }

    public ImplementationExceptionResponse(String message, Throwable cause) {
        super(message, cause);
    }

    public ImplementationExceptionResponse(String message,
            org.fosstrak.epcis.model.ImplementationException implementationException) {
        super(message);
        this.implementationException = implementationException;
    }

    public ImplementationExceptionResponse(String message,
            org.fosstrak.epcis.model.ImplementationException implementationException, Throwable cause) {
        super(message, cause);
        this.implementationException = implementationException;
    }

    public org.fosstrak.epcis.model.ImplementationException getFaultInfo() {
        return this.implementationException;
    }
}
