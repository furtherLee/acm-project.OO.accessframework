package org.acm_project.acm09.OO.epcis.accessframework.japi.captureBroker.eventhandler;

import java.util.TreeMap;

import org.acm_project.acm09.OO.epcis.accessframework.japi.BrokerConfigure;

public class EventXmlHandlerFactory {

	private static EventXmlHandlerFactory instance;

	private static TreeMap<String, String> defaultMap;

	public static synchronized void setInstance(EventXmlHandlerFactory factory) {
		instance = factory;
	}

	public static synchronized EventXmlHandlerFactory getInstance() {
		if (instance == null)
			instance = new EventXmlHandlerFactory();
		return instance;
	}

	/**
	 * 
	 * @param The
	 *            Simple Class Name of the Event Name
	 * @return
	 */
	public EventXmlHandler genHandler(String eventName) {

		String handlerClass = BrokerConfigure.getProperty(eventName);

		if (handlerClass == null)
			handlerClass = defaultMap.get(eventName);

		if (handlerClass == null)
			return null;

		Class<?> cl;

		try {
			cl = Class.forName(handlerClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		EventXmlHandler handler;

		try {
			handler = (EventXmlHandler) cl.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}

		return handler;
	}

	static {
		defaultMap = new TreeMap<String, String>();
		defaultMap
				.put("ObjectEventType",
						"org.acm_project.acm09.OO.epcis.accessframework.japi.captureBroker.eventhandler.ObjectEventXmlHandler");
		defaultMap
				.put("AggregationEventType",
						"org.acm_project.acm09.OO.epcis.accessframework.japi.captureBroker.eventhandler.AggregationEventXmlHandler");
		defaultMap
				.put("QuantityEventType",
						"org.acm_project.acm09.OO.epcis.accessframework.japi.captureBroker.eventhandler.QuantityEventXmlHandler");
		defaultMap
				.put("TransactionEventType",
						"org.acm_project.acm09.OO.epcis.accessframework.japi.captureBroker.eventhandler.TransactionEventXmlHandler");

	}
}
