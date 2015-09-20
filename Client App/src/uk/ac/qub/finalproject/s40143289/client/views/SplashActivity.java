/**
 * 
 */
package uk.ac.qub.finalproject.s40143289.client.views;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import uk.ac.qub.finalproject.s40143289.client.views.R;

/**
 * This is the activity that is first initiated when the app is started. It is
 * used to ensure that a registered user is directed to the Home page and a
 * non-registered user is directed to the Register page.
 * 
 * @author Phil
 *
 */
public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());

		if (pref.getBoolean(getString(R.string.is_registered_key), false)) {
			// open main app
			Intent intent = new Intent(this, MainPage.class);
			startActivity(intent);
		} else {
			// move to the register page
			Intent intent = new Intent(this, RegisterPage.class);
			startActivity(intent);
		}

		finish();

	}

}
