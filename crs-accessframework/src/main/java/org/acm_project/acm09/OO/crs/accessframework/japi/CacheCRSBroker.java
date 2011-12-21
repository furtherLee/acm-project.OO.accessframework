package org.acm_project.acm09.OO.crs.accessframework.japi;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;

public class CacheCRSBroker extends CRSBroker {

    public CacheCRSBroker(String url, int port) {
        super(url, port);
    }
    
    @Override
    public String getEpcisLocation(String epc) {
        return getEpcisLocation(epc, defualtCRSHost, defualtCRSPort);
    }
    
    @Override
    public String getEpcisLocation(String epc, String host, int port) {
        DateAnsPair dateAns = cache.get(epc);
        if (dateAns != null && new Date().getTime() - dateAns.getDate().getTime() < ALLOWED_GAP) {
            return cache.get(epc).getAns();
        } else {
            String ret = super.getEpcisLocation(epc, host, port);
            if (isErrorResult(ret))
                return ret;
            else {
                cache.put(epc, new DateAnsPair(new Date(), ret));
                return ret;
            }
        }
    }
    
    class DateAnsPair {
        public DateAnsPair(Date date, String ans) {
            this.date = date;
            this.ans = ans;
        }
        
        public Date getDate() {
            return date;
        }
        
        public String getAns() {
            return ans;
        }
        
        private Date date;
        private String ans;
    }

    private Map<String, DateAnsPair> cache = new HashMap<String, DateAnsPair>();

    private final static long ALLOWED_GAP = 5 * 60 * 1000;
}
