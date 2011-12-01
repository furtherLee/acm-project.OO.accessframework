package org.acm_project.acm09.OO.crs.accessframework.japi;

import org.acm_project.acm09.OO.crs.accessframework.CRSResolvable;

abstract public class AbstractCRSBroker implements CRSResolvable {

    public boolean isErrorResult(String res) {
        if (res.equals(CONNECTION_ERROR) || res.equals(INVALID_CRSHOST_ERROR) || res.equals(INVALID_EPC_ERROR) || res.equals(TIMEOUT_ERROR))
            return true;
        return false;
    }
    
    public final static String INVALID_EPC_ERROR = "fuck";
    public final static String INVALID_CRSHOST_ERROR = "fuck1";
    public final static String TIMEOUT_ERROR = "fuck2";
    public final static String CONNECTION_ERROR = "fuck3";
    public final static String NO_SUCH_EPC = "fuck4";
}
