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
				+ "\n\nWe know that battery life is important to you, so remember to take a look at the battery settings. You may want to change them."
				+ "\n\nTo find out more about our work or to donate to our project click the donate button below.";
	}

	public static void setDemoHost(String demoHost) {
		HOST = demoHost;
	}
}
