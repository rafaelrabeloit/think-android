package com.think.android.dialog;

import com.think.android.R;
import com.think.android.preference.UserConfiguration;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.TextView;

public class ThemeDialogFragment extends DialogFragment {
	 
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface ThemeDialogListener {
        public void onThemeDialogPositiveClick(DialogInterface dialog, int theme);
    }

    // Use this instance of the interface to deliver action events
    ThemeDialogListener mListener;
    int theme;
    
    public ThemeDialogFragment(int theme) {
    	this.theme = theme;
	}

    public ThemeDialogFragment() {	}

	// Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {        	
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ThemeDialogListener) activity;
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
        View v = inflater.inflate(R.layout.dialog_theme, null);
        
        builder.setView(v)
        	.setTitle(R.string.changing_theme)
               .setPositiveButton(string.ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   mListener.onThemeDialogPositiveClick(dialog, theme);
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   //Não faz nada
                   }
               });
    	
        OnFocusChangeListener click = new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View button, boolean hasFocus) {

				if (hasFocus){
					switch (button.getId()) {
					
					case R.id.layout_think_theme_light:
						theme = UserConfiguration.THEME_LIGHT;
						break;
	
					case R.id.layout_think_theme_dark:
						theme = UserConfiguration.THEME_DARK;
						break;
						
					default:
						break;
					}
				}
			}
		};

		View themeLight = v.findViewById(R.id.layout_think_theme_light);
		
		View themeDark = v.findViewById(R.id.layout_think_theme_dark);

		themeLight.setOnFocusChangeListener(click);
		themeDark.setOnFocusChangeListener(click);
	
		
		if(theme == UserConfiguration.THEME_DARK)	themeDark.requestFocus();
		else										themeLight.requestFocus();
		
    	Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/SEGOESC.TTF");

		((TextView)v.findViewById(R.id.text_think_theme_light)).setTypeface(tf, Typeface.NORMAL);
		((TextView)v.findViewById(R.id.text_think_theme_dark)).setTypeface(tf, Typeface.NORMAL);
        
        // Create the AlertDialog object and return it
        return builder.create();
    }
}