package org.acm_project.acm09.OO.crs.accessframework.webadapter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/crs/{epc}")
public class CRSResource {
	
	@GET
	@Produces("text/plain")
	public String handleGetRequest(@PathParam("epc") String epc){
		StringBuilder ret = new StringBuilder();
		
		
		return ret.toString();
	}
}
