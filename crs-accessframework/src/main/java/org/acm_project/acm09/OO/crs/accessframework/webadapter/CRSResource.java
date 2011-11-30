package org.acm_project.acm09.OO.crs.accessframework.webadapter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.acm_project.acm09.OO.crs.accessframework.CRSResolvable;

@Path("/crs/{epc}")
public class CRSResource {
	
	private static CRSResolvable res;
	public static void setCRS(CRSResolvable resParam){
		res = resParam;
	}
	
	@GET
	public String handleGetRequest(@PathParam("epc") String epc){
		StringBuilder ret = new StringBuilder();
		ret.append(res.getEpcisLocation(epc));
		return ret.toString();
	}
}
