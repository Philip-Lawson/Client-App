/**
 * 
 */
package uk.ac.qub.finalproject.client.views;


import android.os.Bundle;
import android.preference.PreferenceFragment;
import uk.ac.qub.finalproject.client.views.R;

/**
 * @author Phil
 *
 */
public class SettingsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.settings_preferences);	
	}

}
