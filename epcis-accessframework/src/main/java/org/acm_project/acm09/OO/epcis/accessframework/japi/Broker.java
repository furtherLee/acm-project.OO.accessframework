package org.acm_project.acm09.OO.epcis.accessframework.japi;

public abstract class Broker {
	
	public static final String defaultUrl = "http://localhost/epcis-repository/";
	
	private String url = defaultUrl;
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public String getUrl(){
		return url;
	}
	
	public abstract int testLink();
	
	
}
