/**
 * 
 */
package uk.ac.qub.finalproject.client.views;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a utility class that provides a method for validating email formats.
 * 
 * @author Phil
 *
 */
public class EmailValidator {

	private static final String EMAIL_REGEX = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

	/**
	 * Checks to see if the email address is valid. An email address is
	 * considered valid if it conforms to the format of 'abc@ddd.com'. Note that
	 * a + tag is also valid e.g. 'abc+spam@ddd.com'.
	 * 
	 * @param emailAddress
	 * @return
	 */
	public static boolean emailIsValid(String emailAddress) {
		if (null == emailAddress) {
			return false;
		} else {
			Pattern pattern = Pattern.compile(EMAIL_REGEX);
			Matcher matcher = pattern.matcher(emailAddress);

			return matcher.matches();
		}
	}

}
