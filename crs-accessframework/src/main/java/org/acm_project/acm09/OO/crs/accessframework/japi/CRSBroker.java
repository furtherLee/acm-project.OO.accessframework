package org.acm_project.acm09.OO.crs.accessframework.japi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownServiceException;

import org.acm_project.acm09.OO.crs.accessframework.CRSResolvable;

public class CRSBroker extends AbstractCRSBroker {
    
	
	
    private boolean isValidEpc(String epc) {
        if (epc == null)
            return false;
        return epc.trim().startsWith("urn:epc:");
    }
    
    public CRSBroker(String url, int port) {
        defualtCRSHost = url;
        defualtCRSPort = port;
    }
    
    @Override
    public String getEpcisLocation(String epc) {
        return getEpcisLocation(epc, defualtCRSHost, defualtCRSPort);
    }
    
    
	public String getEpcisLocation(String epc, String host, int port) {
		
	    if (!isValidEpc(epc)) {
	        return INVALID_EPC_ERROR;
	    }
	    
	    URL url;
        try {
            url = new URL(host + ":" + port + "/crs/" + epc);
        } catch (MalformedURLException e) {
            return INVALID_CRSHOST_ERROR;
        }
	    
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10);
            connection.connect();
        } catch (Exception e) {
            if (e instanceof SocketTimeoutException) {
                return TIMEOUT_ERROR;
            } else {
                return CONNECTION_ERROR;
            }
        }
        
        String ret = null;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            ret = in.readLine();
            in.close();
        } catch (Exception e) {
            if (e instanceof UnknownServiceException) {
                return INVALID_CRSHOST_ERROR;
            } else {
                return CONNECTION_ERROR;
            }
        }
        
        connection.disconnect();
        
        if (ret.equals("Not Found")) {
            return NO_SUCH_EPC;
        } else {
            return ret;
        }
	}
	
	public static void main(String args[]) {
	    CRSBroker broker = new CRSBroker("http://59.78.28.23", 9998);
	    System.out.println(broker.getEpcisLocation("epc:21213123"));
	}
	
	protected String defualtCRSHost;
	protected int defualtCRSPort;
}
