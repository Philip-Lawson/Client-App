/**
 * 
 */
package com.example.fgjkj;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Phil
 *
 */
public class EmailValidator {

	private static final String EMAIL_REGEX = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

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
