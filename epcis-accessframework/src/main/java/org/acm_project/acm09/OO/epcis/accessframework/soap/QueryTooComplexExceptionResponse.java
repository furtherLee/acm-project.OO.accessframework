package org.acm_project.acm09.OO.epcis.accessframework.soap;

import javax.xml.ws.WebFault;

/**
 * This class was generated by Apache CXF 2.2.9 Tue Jul 06 14:13:27 CEST 2010
 * Generated source version: 2.2.9
 */

@WebFault(name = "QueryTooComplexException", targetNamespace = "urn:epcglobal:epcis-query:xsd:1")
public class QueryTooComplexExceptionResponse extends Exception {
    public static final long serialVersionUID = 20100706141327L;

    private org.fosstrak.epcis.model.QueryTooComplexException queryTooComplexException;

    public QueryTooComplexExceptionResponse() {
        super();
    }

    public QueryTooComplexExceptionResponse(String message) {
        super(message);
    }

    public QueryTooComplexExceptionResponse(String message, Throwable cause) {
        super(message, cause);
    }

    public QueryTooComplexExceptionResponse(String message,
            org.fosstrak.epcis.model.QueryTooComplexException queryTooComplexException) {
        super(message);
        this.queryTooComplexException = queryTooComplexException;
    }

    public QueryTooComplexExceptionResponse(String message,
            org.fosstrak.epcis.model.QueryTooComplexException queryTooComplexException, Throwable cause) {
        super(message, cause);
        this.queryTooComplexException = queryTooComplexException;
    }

    public org.fosstrak.epcis.model.QueryTooComplexException getFaultInfo() {
        return this.queryTooComplexException;
    }
}
