/**
 * 
 */
package com.example.fgjkj;


import android.os.Bundle;

import android.preference.PreferenceFragment;

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
