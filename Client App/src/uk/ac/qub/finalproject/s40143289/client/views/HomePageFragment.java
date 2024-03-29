/**
 * 
 */
package uk.ac.qub.finalproject.s40143289.client.views;

import uk.ac.qub.finalproject.client.services.BatteryMonitorService;
import uk.ac.qub.finalproject.client.services.ChangeEmailAddressService;
import uk.ac.qub.finalproject.client.services.DataProcessingService;
import uk.ac.qub.finalproject.client.services.RequestWorkPacketsService;
import uk.ac.qub.finalproject.s40143289.client.views.R;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This layout fragment shows a 'My Account' screen. From this screen the user
 * can see how many work packets have been processed, they can start or stop
 * background processing and they can change their email and/or anonymyous
 * status.
 * 
 * @author Phil
 *
 */
public class HomePageFragment extends Fragment {

	private TextView numPacketsProcessedText;
	private Button startStopProcessing;
	private TextView emailText;
	private CheckedTextView anonymousCheckedTextView;
	private Button changeEmailButton;
	private SharedPreferences.OnSharedPreferenceChangeListener prefListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_page_fragment, container,
				false);
		setupPacketsProcessedTextView(view);
		setupProcessingButton(view);
		setupEmailTextView(view);
		setupChangeEmailButton(view);

		return view;
	}

	@Override
	public void onDestroy() {
		deregisterPacketsProcessedListener();
		super.onDestroy();
	}

	/**
	 * Helper method that sets up the text view. It dynamically writes the
	 * number of packets processed from a field in shared preferences.
	 * 
	 * @param view
	 */
	private void setupPacketsProcessedTextView(View view) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getActivity());

		numPacketsProcessedText = (TextView) view
				.findViewById(R.string.home_page_num_packets_processed_id);
		int packetsProcessed = pref.getInt(
				getString(R.string.packets_completed_key), 0);
		String text = packetsProcessed
				+ " "
				+ getString(R.string.home_page_num_packets_processed_text_part2);

		numPacketsProcessedText.setText(text);
	}

	/**
	 * Sets up the email text view and the checked text view underneath. This
	 * represents the user's current email address and their anonymity status.
	 * 
	 * @param view
	 */
	private void setupEmailTextView(View view) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		String email = pref.getString(getString(R.string.email_key), "");
		boolean anonymous = pref.getBoolean(getString(R.string.anonymous_key),
				false);

		emailText = (TextView) view
				.findViewById(R.string.home_page_email_text_id);
		anonymousCheckedTextView = (CheckedTextView) view
				.findViewById(R.string.home_page_anonymous_user_checked_text_view_id);

		if (email.equals("")) {
			emailText.setText(getString(R.string.home_page_email_default_text));
		} else {
			emailText.setText(email);
		}

		anonymousCheckedTextView.setEnabled(true);
		anonymousCheckedTextView.setClickable(true);
		anonymousCheckedTextView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				openChangeEmailDialog();				
			}
			
		});
		anonymousCheckedTextView.setChecked(anonymous);

	}

	/**
	 * Configures the change email button and adds an onClickListener to the
	 * button.
	 * 
	 * @param view
	 */
	private void setupChangeEmailButton(View view) {
		changeEmailButton = (Button) view
				.findViewById(R.string.home_page_change_email_button_id);
		changeEmailButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openChangeEmailDialog();
			}

		});
	}

	/**
	 * Configures the processing button. Adds an onClickListener that will
	 * change the text of the button based on the current processing status and
	 * will either start or stop background processing.<br>
	 * </br> e.g. If the button says 'Stop Processing', when it is clicked all
	 * processing will be stopped and the text of the button will change to
	 * 'Start Processing'.
	 * 
	 * 
	 * @param view
	 */
	private void setupProcessingButton(View view) {
		startStopProcessing = (Button) view
				.findViewById(R.string.home_page_processing_button_id);

		if (isProcessingRunning(DataProcessingService.class)) {
			registerPacketsProcessedListener();
			startStopProcessing
					.setText(getString(R.string.home_page_processing_button_stop_processing_text));
		} else if (beginButtonPushed()) {
			registerPacketsProcessedListener();
			startStopProcessing
					.setText(getString(R.string.home_page_processing_button_start_processing_text));
		} else {
			startStopProcessing
					.setText(getString(R.string.home_page_processing_button_startup_text));
		}

		startStopProcessing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String stopProcessing = getString(R.string.home_page_processing_button_stop_processing_text);

				if (!hasStartedProcessing()) {
					Intent getPackets = new Intent(getActivity(),
							RequestWorkPacketsService.class);
					getActivity().startService(getPackets);

					beginButtonWasPushed();
					changeUserPermitsProcessingPref(true);
					registerPacketsProcessedListener();
					startStopProcessing
							.setText(getString(R.string.home_page_processing_button_stop_processing_text));

				} else if (startStopProcessing.getText().equals(stopProcessing)) {
					Toast.makeText(getActivity(), "Processing Stopped",
							Toast.LENGTH_SHORT).show();
					Intent stopListeningToBattery = new Intent(getActivity(),
							BatteryMonitorService.class);
					Intent stopProcessingService = new Intent(getActivity(),
							DataProcessingService.class);

					changeUserPermitsProcessingPref(false);
					getActivity().stopService(stopListeningToBattery);
					getActivity().stopService(stopProcessingService);
					deregisterPacketsProcessedListener();

					startStopProcessing
							.setText(getString(R.string.home_page_processing_button_start_processing_text));
				} else {
					Toast.makeText(getActivity(), "Processing Started",
							Toast.LENGTH_SHORT).show();

					Intent startBatteryMonitor = new Intent(getActivity(),
							BatteryMonitorService.class);

					changeUserPermitsProcessingPref(true);
					getActivity().startService(startBatteryMonitor);
					registerPacketsProcessedListener();

					startStopProcessing
							.setText(getString(R.string.home_page_processing_button_stop_processing_text));
				}

			}

		});
	}

	/**
	 * Opens a dialog box with a text field and a checkbox to allow the user to
	 * change their email address or their anonymous status. This includes
	 * validation for the new email address.<br>
	 * </br>The ChangeEmailAddressService will only be triggered if the email
	 * address is changed to something new, or if the user changes their
	 * anonymous status.
	 */
	private void openChangeEmailDialog() {
		// inflate the view and set up the textfield and checked text view
		LayoutInflater factory = LayoutInflater.from(getActivity());
		ViewGroup parent = (ViewGroup) getActivity().findViewById(
				R.layout.home_page_fragment);

		final View changeEmailView = factory.inflate(
				R.layout.change_email_dialog_view, parent);

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
				getActivity());

		dialogBuilder.setTitle("Enter Email");

		dialogBuilder.setView(changeEmailView);
		dialogBuilder.create();

		final EditText changeEmailText = (EditText) changeEmailView
				.findViewById(R.string.change_email_edit_text_id);
		final CheckedTextView isAnonymousCheckBox = (CheckedTextView) changeEmailView
				.findViewById(R.string.change_email_remain_anonymous_check_id);

		// sets the text and the checked text to the user's current preferences
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		String email = pref.getString(getString(R.string.email_key), "");
		boolean isAnonymous = pref.getBoolean(
				getString(R.string.anonymous_key), false);

		if (email.equals("")) {
			changeEmailText.setText("");
			// .setText(getString(R.string.home_page_email_default_text));
		} else {
			changeEmailText.setText(email);
		}

		isAnonymousCheckBox.setChecked(isAnonymous);

		if (isAnonymous) {
			changeEmailText.setEnabled(false);
		}

		// set listeners
		isAnonymousCheckBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isAnonymousCheckBox.toggle();
				changeEmailText.setEnabled(!isAnonymousCheckBox.isChecked());
			}
		});

		dialogBuilder.setPositiveButton("Save",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// retrieve user details and their new email address
						SharedPreferences pref = PreferenceManager
								.getDefaultSharedPreferences(getActivity());
						String newEmail = changeEmailText.getText().toString();
						String oldEmail = pref.getString(
								getString(R.string.email_key), "old email");
						boolean userWasAnonymous = pref.getBoolean(
								getString(R.string.anonymous_key), false);

						if (isAnonymousCheckBox.isChecked()) {
							// starts the change email service if
							// the user's anonymous status has changed
							anonymousCheckedTextView.setChecked(true);

							if (!userWasAnonymous) {
								pref.edit()
										.putBoolean(
												getString(R.string.anonymous_key),
												true).apply();
								startChangeEmailService();
							}

							dialog.dismiss();
						} else if (EmailValidator.emailIsValid(newEmail)) {
							// change the properties on the page
							emailText.setText(newEmail);
							pref.edit()
									.putBoolean(
											getString(R.string.anonymous_key),
											false).apply();
							anonymousCheckedTextView.setChecked(false);

							// if there is a new email address or the user was
							// anonymous the change email address service is
							// started
							if (!oldEmail.equals(newEmail) || userWasAnonymous) {
								pref.edit()
										.putString(
												getString(R.string.email_key),
												newEmail).apply();
								startChangeEmailService();
							}

							dialog.dismiss();
						} else {
							// notify the user that they entered an invalid
							// email
							FragmentTransaction fragment = getFragmentManager()
									.beginTransaction();
							DialogFragment invalidEmailDialog = new InvalidEmailDialogFragment();
							invalidEmailDialog.show(fragment, "invalidEmail");
						}
					}

				});

		dialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		dialogBuilder.show();
	}

	/**
	 * Starts the background service to change the user's email details on the
	 * server.
	 */
	private void startChangeEmailService() {
		Intent changeEmailService = new Intent(getActivity(),
				ChangeEmailAddressService.class);
		getActivity().startService(changeEmailService);
	}

	/**
	 * Changes the user's permission to process data.
	 * 
	 * @param permission
	 */
	private void changeUserPermitsProcessingPref(boolean permission) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		pref.edit()
				.putBoolean(getString(R.string.user_permits_processing_key),
						permission).apply();

	}

	/**
	 * Checks to see if background processing is running on startup.
	 * 
	 * @param serviceClass
	 * @return
	 */
	private boolean isProcessingRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) getActivity()
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Helper method registers the listener that listens for changes in the
	 * number of packets processed.
	 */
	private void registerPacketsProcessedListener() {		
		// adds a change listener to shared preferences
		// this listens for changes to the number of packets
		// processed and changes the text field when the number changes
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());				
		prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {

			@Override
			public void onSharedPreferenceChanged(
					SharedPreferences sharedPreferences, String key) {
				if (key.equals(getString(R.string.packets_completed_key))) {
					String packetsProcessed = sharedPreferences.getInt(
							getString(R.string.packets_completed_key), 0)
							+ " "
							+ getString(R.string.home_page_num_packets_processed_text_part2);

					numPacketsProcessedText.setText(packetsProcessed);
				}

			}
		};

		pref.registerOnSharedPreferenceChangeListener(prefListener);
	}

	/**
	 * Helper method deregisters the listener that listens for changes in the
	 * number of packets processed.
	 */
	private void deregisterPacketsProcessedListener() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		pref.unregisterOnSharedPreferenceChangeListener(prefListener);
	}

	/**
	 * Helper method stores a flag to show that the begin button has been
	 * pushed. This button should only be pushed once, even if processing hasn't
	 * yet occurred.
	 */
	private void beginButtonWasPushed() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		pref.edit().putBoolean(getString(R.string.begin_button_pushed), true)
				.apply();
	}

	private boolean beginButtonPushed() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		return pref.getBoolean(getString(R.string.begin_button_pushed), false);
	}

	/**
	 * Helper method checks to see if the app has started processing
	 * 
	 * @return
	 */
	private boolean hasStartedProcessing() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		return pref.getBoolean(getString(R.string.processing_started_key),
				false);
	}
}
