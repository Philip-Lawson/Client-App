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

	public static IDataProcessor getDataProcessor() {
		return new DummyProcessor();
	}
	
	public static boolean donateButtonEnabled(){
		return true;
	}
	
	public static String getProjectWebsite(){
		return "http://oxfam.org.uk";
	}
}
