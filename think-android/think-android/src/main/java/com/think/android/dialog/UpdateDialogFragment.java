package com.think.android.dialog;

import com.think.android.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

public class UpdateDialogFragment extends DialogFragment {
	 
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface UpdateDialogListener {
        public void onUpdateDialogPositiveClick(DialogInterface dialog, int updateTime);
    }

    // Use this instance of the interface to deliver action events
    UpdateDialogListener mListener;
    
    public UpdateDialogFragment(){}
    
	// Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {        	
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (UpdateDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
  
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        inflater.inflate(R.layout.dialog_update, null);
        
        builder.setTitle(R.string.changing_update_time)
	        .setItems(R.array.array_string_update_time, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	mListener.onUpdateDialogPositiveClick(dialog, which);
	        }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}