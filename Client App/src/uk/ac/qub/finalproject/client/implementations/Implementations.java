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
		
}
