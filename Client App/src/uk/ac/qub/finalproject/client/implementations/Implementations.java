/**
 * 
 */
package uk.ac.qub.finalproject.client.implementations;

import uk.ac.qub.finalproject.calculationclasses.IDataProcessor;

/**
 * This is the virtual config file of the application. The data processor
 * implementation must be placed here along with the about text, project website
 * and the IP address and port of the server. Note that when the boolean isDemo
 * is set to true, an extra button and textfield appears on the Register page
 * allowing the server IP address to be specified dynamically.
 * 
 * @author Phil
 *
 */
public class Implementations {

	private static String HOST = "10.0.2.2";

	public static boolean isDemo = true;

	public static IDataProcessor getDataProcessor() {
		return new DummyProcessor();
	}

	public static boolean donateButtonEnabled() {
		return true;
	}

	public static String getProjectWebsite() {
		return "http://oxfam.org.uk";
	}

	public static final int getPortNumber() {
		return 12346;
	}

	public static final String getHost() {
		return HOST;
	}

	public static String getAboutText() {
		return "Citizen Science is an app that allows you to process scientific data during idle time on your device."
				+ " By donating your CPU to our project you are helping to bring about the day when we finally understand how to multiply numbers very slowly."
				+ "\n\nWe know that battery life is important to you, so remember to take a look at the battery settings."
				+ "\n\nTo find out more about our work or to donate to our project click the donate button below.";
	}

	public static void setDemoHost(String demoHost) {
		HOST = demoHost;
	}
}
