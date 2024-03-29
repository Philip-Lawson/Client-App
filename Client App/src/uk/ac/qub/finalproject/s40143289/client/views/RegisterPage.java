package uk.ac.qub.finalproject.s40143289.client.views;

import uk.ac.qub.finalproject.calculationclasses.RegistrationPack;
import uk.ac.qub.finalproject.client.implementations.Implementations;
import uk.ac.qub.finalproject.client.persistence.FileAndPrefStorage;
import uk.ac.qub.finalproject.client.services.RunnableClientTemplate;
import uk.ac.qub.finalproject.s40143289.client.views.R;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * The register page is the first point of contact to the app for a new user. It
 * allows the user to register with or without an email. The user must register
 * before they are permitted to move to the home page.
 * 
 * @author Phil
 *
 */
@SuppressWarnings("deprecation")
public class RegisterPage extends ActionBarActivity {

	public static String REGISTRATION_SUCCESS = "Registration Successful?";

	private CheckedTextView anonymousField;
	private EditText emailAddressText;
	private Button registerButton;
	private ProgressDialog registrationDialog;
	private EditText demoURL;
	private Button saveDemoUrl;

	/**
	 * This handler is registered with the registration thread. The registration
	 * thread calls handle message when registration is complete.
	 */
	private final Handler registrationHandler = new Handler(
			Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle b = msg.getData();
			handleRegistration(b.getBoolean(REGISTRATION_SUCCESS));
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_page);
		PreferenceManager.setDefaultValues(this, R.xml.settings_preferences,
				false);

		setupPersistence();
		setupAnonymousField();
		setupEmailAddressText();
		setupRegisterButton();
		setupDemoURLWidgets();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Helper method sets up the anonymous field and adds a listener to the
	 * checked text view.
	 */
	private void setupAnonymousField() {
		anonymousField = (CheckedTextView) findViewById(R.string.register_anonymously_checked_textview_id);
		anonymousField.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				anonymousField.toggle();
				if (anonymousField.isChecked()) {
					emailAddressText.setText("");
					emailAddressText.setEnabled(false);
				} else {
					emailAddressText.setEnabled(true);
				}

			}

		});
	}

	/**
	 * Helper method sets up the email address text view and sets its action
	 * listener.
	 */
	private void setupEmailAddressText() {
		emailAddressText = (EditText) findViewById(R.string.register_email_field_id);
		emailAddressText
				.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						String emailAddress = v.getText().toString();

						if (EmailValidator.emailIsValid(emailAddress)) {
							// return to the main menu
							return false;
						} else {
							showInvalidEmailNotification();
							// returns the user to the text view
							return true;
						}
					}

				});
	}

	/**
	 * Helper method sets up the register button and sets its action listener.
	 */
	private void setupRegisterButton() {
		registerButton = (Button) findViewById(R.string.register_button_id);
		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String emailAddress = emailAddressText.getText().toString();
				boolean validEmailAddress = EmailValidator
						.emailIsValid(emailAddress);

				if (anonymousField.isChecked() || validEmailAddress) {

					SharedPreferences pref = PreferenceManager
							.getDefaultSharedPreferences(getApplicationContext());

					RegistrationPack registrationPack = new RegistrationPack();
					registrationPack.setAndroidID(Secure.getString(
							getContentResolver(), Secure.ANDROID_ID));
					registrationPack.setVersionCode(getAppVersion());					
					

					if (anonymousField.isChecked()) {
						// store the user's preference for anonymity in shared
						// preferences
						pref.edit()
								.putBoolean(getString(R.string.anonymous_key),
										true).apply();
					} else {
						registrationPack.setEmailAddress(emailAddress);

						// store the user's preference for anonymity and their
						// email
						// address in shared preferences
						pref.edit()
								.putBoolean(getString(R.string.anonymous_key),
										false).apply();
						pref.edit()
								.putString(getString(R.string.email_key),
										emailAddress).apply();

					}
					
					startRegistration(registrationPack);
					showProgress();
				} else {
					// notify the user of an invalid email address
					showInvalidEmailNotification();
				}

			}

		});
	}

	/**
	 * Helper method sets up the persistence layer.
	 */
	private void setupPersistence() {
		FileAndPrefStorage persistenceLayer = FileAndPrefStorage
				.getInstance(this);
		persistenceLayer.setupStorage();
	}

	/**
	 * Helper method to set up demo widgets used to set the networking URL
	 * during a demo.
	 */
	private void setupDemoURLWidgets() {
		if (Implementations.isDemo){
			demoURL = (EditText) findViewById(R.string.demo_url_edit_text_id);
			demoURL.setEnabled(true);
			demoURL.setVisibility(View.VISIBLE);
			
			saveDemoUrl = (Button) findViewById(R.string.demo_url_button_id);
			saveDemoUrl.setText("SAVE URL");
			saveDemoUrl.setEnabled(true);
			saveDemoUrl.setVisibility(View.VISIBLE);
			saveDemoUrl.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Implementations.setDemoHost(demoURL.getText().toString());					
				}
			});
		}
		
		

	}

	/**
	 * Shows a notification that the email entered is invalid.
	 */
	private void showInvalidEmailNotification() {
		FragmentTransaction fragment = getFragmentManager().beginTransaction();
		DialogFragment invalidEmailDialog = new InvalidEmailDialogFragment();
		invalidEmailDialog.show(fragment, "invalidEmail");
	}

	/**
	 * Responds to the registration event.
	 * 
	 * @param registrationSuccess
	 *            if the registration was successful.
	 */
	private void handleRegistration(boolean registrationSuccess) {
		if (registrationDialog.isShowing()) {
			registrationDialog.dismiss();
		}

		FragmentTransaction fragment = getFragmentManager().beginTransaction();
		DialogFragment registrationMessage = new RegistrationSuccessDialogFragment(
				registrationSuccess, this);
		registrationMessage.show(fragment, "registration");

	}

	/**
	 * Starts the networking thread that will register the device with the
	 * server.
	 * 
	 * @param registrationPack
	 */
	private void startRegistration(RegistrationPack registrationPack) {
		// locks the screen during networking to avoid accidentally cancelling
		// the networking thread
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

		RunnableClientTemplate registerClient = new RegisterClientThread(
				getApplicationContext(), registrationPack, registrationHandler);
		Thread registrationThread = new Thread(registerClient);
		registrationThread.setDaemon(true);		
		registrationThread.start();
		
	}

	/**
	 * Shows a progress dialog for registration in case the network is slow and
	 * the user needs a visual cue.
	 */
	private void showProgress() {
		registrationDialog = ProgressDialog.show(this,
				getString(R.string.registration_dialog_title),
				getString(R.string.registration_dialog_text));
	}

	/**
	 * If the user has previously registered and de-registered on this device
	 * the network broadcaster will have been disabled. This method ensures the
	 * receiver is enabled.
	 */
	private void wakeUpNetworkBroadcastReceiver() {
		ComponentName component = new ComponentName(this,
				NetworkInfoReceiver.class);

		int status = getPackageManager().getComponentEnabledSetting(component);
		if (status == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
			getPackageManager().setComponentEnabledSetting(component,
					PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
					PackageManager.DONT_KILL_APP);
		}
	}

	/**
	 * Move to the next page upon successful registration.
	 */
	private void moveToNextPage() {
		Intent intent = new Intent(this, MainPage.class);
		startActivity(intent);

		/*
		 * called to make sure the user can't go back to the register page if
		 * they want to change their details they can do so in the settings page
		 */
		finish();
	}

	/**
	 * Helper method to get the current version that this device is using.
	 * 
	 * @return
	 */
	private int getAppVersion() {
		try {
			return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			return 0;
		}
	}

	/**
	 * A Dialog Fragment inner class. This shows a message to the user once a
	 * registration attempt has been completed. If the registration was a
	 * success, the user is shown a success message and will be directed to the
	 * main page of the app, otherwise the user is shown a registration
	 * unsuccessful message and will remain on the registration page.
	 * 
	 * @author Phil
	 *
	 */
	class RegistrationSuccessDialogFragment extends DialogFragment {

		private boolean registrationSuccess;

		public RegistrationSuccessDialogFragment(boolean registrationSuccess,
				RegisterPage main) {
			super();
			this.registrationSuccess = registrationSuccess;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			String title, text;
			if (registrationSuccess) {
				// store a success flag in the shared preferences
				// this allows the splash activity to move the user directly to
				// the main app page
				SharedPreferences pref = PreferenceManager
						.getDefaultSharedPreferences(getBaseContext());
				pref.edit()
						.putBoolean(getString(R.string.is_registered_key), true)
						.apply();

				title = getString(R.string.registration_successful_title);
				text = getString(R.string.registration_successful_text);
			} else {
				title = getString(R.string.registration_unsuccessful_title);
				text = getString(R.string.registration_unsuccessful_text);
			}

			// build the dialog based on the values set above
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(text);
			builder.setTitle(title);
			builder.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();

							// unlocks the screen after the networking thread
							// has completed
							setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

							if (registrationSuccess) {
								// if the user is not registering for the first
								// time on this device the network broadcast
								// receiver will have been disabled
								wakeUpNetworkBroadcastReceiver();
								moveToNextPage();
							}
						}
					});

			return builder.create();
		}

	}

}
