/**
 * 
 */
package finalproject.poc.calculationclasses;

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
	protected Object processData(Object obj) {
		// TODO Auto-generated method stub
		Integer number;
		try {
			number = (Integer) obj;
		} catch (ClassCastException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		return number*2;
	}

}
