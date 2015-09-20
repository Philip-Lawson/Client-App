/**
 * 
 */
package uk.ac.qub.finalproject.client.implementations;

import uk.ac.qub.finalproject.calculationclasses.IDataProcessor;

/**
 * @author Phil
 *
 */
public class Implementations {
	
	private static String HOST = "10.0.2.2";
	
	public static boolean isDemo = false;

	public static IDataProcessor getDataProcessor() {
		return new DummyProcessor();
	}
	
	public static final int getPortNumber(){
		return 12346;	
	}
	
	public static final String getHost(){		
		return HOST;		
	}
	
	public static boolean donateButtonEnabled(){
		return true;
	}
	
	public static String getProjectWebsite(){
		return "http://oxfam.org.uk";
	}
	
	public static void setDemoHost(String demoHost){
		HOST = demoHost;
	}
}
