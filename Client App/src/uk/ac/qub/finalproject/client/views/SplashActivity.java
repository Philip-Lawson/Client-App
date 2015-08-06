/**
 * 
 */
package uk.ac.qub.finalproject.client.views;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import uk.ac.qub.finalproject.client.views.R;

/**
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
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}

		finish();

	}

}
