package org.acm_project.acm09.OO.crs.accessframework.webadapter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.acm_project.acm09.OO.crs.accessframework.CRSResolvable;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;

public class CRSWebAdapter {

	private String hostPath;

	private boolean started;

	private SelectorThread threadSelector;

	private Map<String, String> initParams;

	public CRSWebAdapter(String hostPath, CRSResolvable comp) {
		this.hostPath = hostPath;
		initParams = new HashMap<String, String>();
		initParams
				.put("com.sun.jersey.config.property.packages",
						"org.acm_project.acm09.OO.crs.accessframework.webadapter");
		CRSResource.setCRS(comp);
	}

	public void setHostPath(String hostPath) {
		this.hostPath = hostPath;
	}

	public void start() {
		System.out.println("CRSWebAdapter is starting...");
		try {
			threadSelector = GrizzlyWebContainerFactory.create(
					hostPath, initParams);
		} catch (IllegalArgumentException e) {
			System.out.println("Starting Failed!");
			e.printStackTrace();
			started = false;
		} catch (IOException e) {
			System.out.println("Starting Failed!");
			e.printStackTrace();
			started = false;
		}
		System.out.println("CRSWebAdapter is started!");
		started = true;
	}

	public void stop() {
		System.out.println("CRSWebAdapter shut down!");
		threadSelector.stopEndpoint();
		threadSelector = null;
		started = false;
	}

	public boolean isRunning() {
		return this.started;
	}

}
