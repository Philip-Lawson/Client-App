/**
 * 
 */
package uk.ac.qub.finalproject.s40143289.client.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import uk.ac.qub.finalproject.s40143289.client.views.R;

/**
 * A ready made dialog fragment that informs the user that a network action
 * cannot be performed right now due to the network being unavailable.
 * 
 * @author Phil
 *
 */
public class NetworkUnavailableDialogFragment extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.network_unavailable_text);
		builder.setTitle(R.string.network_unavailable_title);
		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		return builder.create();
	}
}
