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
		// TODO Auto-generated method stub
		Integer number;
		try {
			number = (Integer) obj;
		} catch (ClassCastException e) {			
			return -1;
		}
		
		return number*2;
	}

}
