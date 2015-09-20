/**
 * 
 */
package uk.ac.qub.finalproject.s40143289.client.views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import uk.ac.qub.finalproject.client.persistence.FileAndPrefStorage;
import uk.ac.qub.finalproject.s40143289.client.views.R;

/**
 * The SettingsFragment allows the user to change their settings. It extends the
 * preference fragment class and uses the built in preference widgets to
 * maintain a look and feel consistent with other apps. It uses a change
 * listener to transfer files if the user changes their storage preference.
 * 
 * @author Phil
 *
 */
public class SettingsFragment extends PreferenceFragment {

	private SharedPreferences.OnSharedPreferenceChangeListener prefListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings_preferences);
		setupPreferenceListener();
	}

	@Override
	public void onDestroy() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		prefs.unregisterOnSharedPreferenceChangeListener(prefListener);
		super.onDestroy();
	}

	private void setupPreferenceListener() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());

		prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {

			@Override
			public void onSharedPreferenceChanged(
					SharedPreferences sharedPreferences, String key) {
				if (key.equals(getString(R.string.storage_key))) {
					FileAndPrefStorage database = FileAndPrefStorage
							.getInstance(getActivity());
					database.transferFiles();
				}

			}
		};

		prefs.registerOnSharedPreferenceChangeListener(prefListener);
	}

}
