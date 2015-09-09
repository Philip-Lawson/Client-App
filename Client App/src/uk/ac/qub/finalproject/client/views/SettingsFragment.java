/**
 * 
 */
package uk.ac.qub.finalproject.client.views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import uk.ac.qub.finalproject.client.persistence.FileAndPrefStorage;
import uk.ac.qub.finalproject.client.views.R;

/**
 * @author Phil
 *
 */
public class SettingsFragment extends PreferenceFragment {

	private SharedPreferences.OnSharedPreferenceChangeListener prefListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings_preferences);

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

	@Override
	public void onDestroy() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		prefs.unregisterOnSharedPreferenceChangeListener(prefListener);
		super.onDestroy();
	}

}
