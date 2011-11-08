package org.acm_project.acm09.OO.epcis.accessframework.japi;

abstract class Broker {
	
	public static final String defaultUrl = "http://localhost/epcis-repository/capture";
	
	private String url = defaultUrl;
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public String getUrl(){
		return url;
	}
	
	abstract int testLink();
	
	
}
