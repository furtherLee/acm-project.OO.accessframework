package org.acm_project.acm09.OO.crs.accessframework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestQuery extends TestCase{
	
	public TestQuery(String testName){
		super(testName);
	}
	
	public void testGetEpcisLocation(){
		String temp = null;
		for (int i = 0 ; i < 10000; ++i)
			try {
				HttpURLConnection u = (HttpURLConnection) new URL("http://localhost:9998/crs/123").openConnection();
				u.connect();
				BufferedReader reader = new BufferedReader(new InputStreamReader(u.getInputStream()));
				temp = reader.readLine();
				u.disconnect();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				assertTrue(temp.equals("uncle fucker"));
			}
	}
	
	public static Test suite(){
		return new TestSuite(TestQuery.class);
	}
	
	public static void main(String args[]){
		junit.textui.TestRunner.run(suite());
	}
}
