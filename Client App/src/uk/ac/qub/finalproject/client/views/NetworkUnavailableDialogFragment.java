/**
 * 
 */
package uk.ac.qub.finalproject.client.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import uk.ac.qub.finalproject.client.views.R;

/**
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
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		
		return builder.create();
	}
}
