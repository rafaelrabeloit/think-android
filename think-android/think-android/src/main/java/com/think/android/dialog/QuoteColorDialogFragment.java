package com.think.android.dialog;

import com.think.android.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;

public class QuoteColorDialogFragment extends DialogFragment {
	 
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface QuoteColorDialogListener {
        public void onQuoteColorDialogPositiveClick(DialogInterface dialog, int quoteColor, boolean all);
    }

    // Use this instance of the interface to deliver action events
    QuoteColorDialogListener mListener;
    int quoteColor;
    
    public QuoteColorDialogFragment(int quoteColor) {
    	this.quoteColor = quoteColor;
	}

    public QuoteColorDialogFragment() {	}

	// Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {        	
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (QuoteColorDialogListener) activity;
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
        View v = inflater.inflate(R.layout.dialog_quote_color, null);
        
        builder.setView(v)
        	.setTitle(R.string.changing_quote_color)
               .setPositiveButton(R.string.all_quotes, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   mListener.onQuoteColorDialogPositiveClick(dialog, quoteColor, true);
                   }
               }).setNeutralButton(R.string.only_new_quotes, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   mListener.onQuoteColorDialogPositiveClick(dialog, quoteColor, false);
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   //Nothing happens
                   }
               });
    	
        OnFocusChangeListener click = new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View button, boolean hasFocus) {

				if (hasFocus){
					switch (button.getId()) {
					
					case R.id.image_think_red:
						quoteColor = R.color.red_think;
						break;
	
					case R.id.image_think_transparent:
						quoteColor = android.R.color.transparent;
						break;
	
					case R.id.image_think_blue:
						quoteColor = R.color.blue_think;
						break;
	
					case R.id.image_think_green:
						quoteColor = R.color.green_think;
						break;
	
					case R.id.image_think_yellow:
						quoteColor = R.color.yellow_think;
						break;
	
					case R.id.image_think_purple:
						quoteColor = R.color.purple_think;
						break;
	
					case R.id.image_think_orange:
						quoteColor = R.color.orange_think;
						break;
					default:
						break;
					}
				}
			}
		};

		View blue = v.findViewById(R.id.image_think_blue);
		View green = v.findViewById(R.id.image_think_green);
		View red = v.findViewById(R.id.image_think_red);
		View orange = v.findViewById(R.id.image_think_orange);
		View yellow = v.findViewById(R.id.image_think_yellow);
		View purple = v.findViewById(R.id.image_think_purple);
		View transparent = v.findViewById(R.id.image_think_transparent);
		
		blue.setOnFocusChangeListener(click);
		green.setOnFocusChangeListener(click);
		red.setOnFocusChangeListener(click);
		orange.setOnFocusChangeListener(click);
		yellow.setOnFocusChangeListener(click);
		purple.setOnFocusChangeListener(click);
		transparent.setOnFocusChangeListener(click);
		
		switch (quoteColor) {
		
		case R.color.red_think:
			red.requestFocus();
			break;

		case android.R.color.transparent:
			transparent.requestFocus();
			break;

		case R.color.blue_think:
			blue.requestFocus();
			break;

		case R.color.green_think:
			green.requestFocus();
			break;

		case R.color.yellow_think:
			yellow.requestFocus();
			break;

		case R.color.purple_think:
			purple.requestFocus();
			break;

		case R.color.orange_think:
			orange.requestFocus();
			break;
		default:
			break;
		}
        
        // Create the AlertDialog object and return it
        return builder.create();
    }
}