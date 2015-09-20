/**
 * 
 */
package uk.ac.qub.finalproject.client.implementations;

import java.io.Serializable;

import uk.ac.qub.finalproject.calculationclasses.AbstractDataProcessor;

/**
 * A dummy implementation of the data processor class.
 * @author Phil
 *
 */
public class DummyProcessor extends AbstractDataProcessor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1696565467124647685L;

	@Override
	protected Serializable processData(Serializable obj) {
		
		Integer number;
		try {
			number = (Integer) obj;
			Thread.sleep(10000);
		} catch (ClassCastException e) {			
			return -1;
		} catch (InterruptedException e){
			return -1;
		}
		
		return number*2;
	}

}
