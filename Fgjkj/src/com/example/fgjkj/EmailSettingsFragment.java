/**
 * 
 */
package com.example.fgjkj;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.app.FragmentTransaction;

/**
 * @author Phil
 *
 */
public class EmailSettingsFragment extends PreferenceFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.email_preferences);	
		
		final CheckBoxPreference anonymousPreference = (CheckBoxPreference) findPreference(getString(R.string.anonymous_key));
		final EditTextPreference emailAddressPreference = (EditTextPreference) findPreference(getString(R.string.email_key));

		anonymousPreference
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference pref,
							Object newValue) {
						boolean anonymous = (Boolean) newValue;

						if (!anonymous) {
							emailAddressPreference.setEnabled(true);
						} else {
							emailAddressPreference.setText("");
							emailAddressPreference
									.setSummary("Email Address");
							emailAddressPreference.setEnabled(false);
						}
						return true;
					}
				});

		emailAddressPreference
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference pref,
							Object newValue) {

						String emailAddress = (String) newValue;

						if (emailAddress.equals("")) {
							return true;
						} else if (EmailValidator.emailIsValid(emailAddress)) {
							emailAddressPreference.setSummary(emailAddress);
							return true;
						} else {
							// notify the user of an invalid email address
							FragmentTransaction fragment = getFragmentManager()
									.beginTransaction();
							DialogFragment invalidEmailDialog = new InvalidEmailDialogFragment();
							invalidEmailDialog.show(fragment, "invalidEmail");
							return false;
						}
					}
				});

		if (anonymousPreference.isChecked()) {
			emailAddressPreference.setText("");
			emailAddressPreference.setEnabled(false);
		} else {
			SharedPreferences pref = PreferenceManager
					.getDefaultSharedPreferences(getActivity());
			String emailAddress = pref.getString(getString(R.string.email_key),
					"");
			emailAddressPreference.setSummary(emailAddress);
		}
	}

}
