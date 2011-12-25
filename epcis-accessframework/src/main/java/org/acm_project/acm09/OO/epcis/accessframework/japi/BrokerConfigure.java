package org.acm_project.acm09.OO.epcis.accessframework.japi;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BrokerConfigure {
	
	private static Properties prop;
	
	private static final String confFileName = "epcisbroker.properties";
	
	static{
		prop = new Properties();
		try{
			prop.load(new FileInputStream(confFileName));
		}
		catch(IOException e){
			System.err.println("load properties error. Valid Property File Named epcisbroker.properties");
		}
	}
	
	public static String getProperty(String key){
		return prop.getProperty(key);
	}
	
	
}
