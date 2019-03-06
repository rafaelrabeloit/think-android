package com.think.android.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.think.model.Language;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.think.android.R;
import com.think.android.preference.UserConfiguration;

@EViewGroup(R.layout.item_view_language)
public class LanguageItemView extends LinearLayout {
	
	@Bean
	UserConfiguration config;
	
	@ViewById(R.id.text_language_description)
	TextView textLanguageDescription;

	@ViewById(R.id.text_language_code)
	TextView textLanguageCode;
	
	public LanguageItemView(Context context) {
	    super(context);
	}

	/**
	 */
	public void bind(Language language) {
		        
        if (textLanguageDescription != null)  {
        	textLanguageDescription.setClickable(false);
        	textLanguageDescription.setFocusable(false);
        	textLanguageDescription.setFocusableInTouchMode(false);
        	
        	textLanguageDescription.setText( language.getName() ); 
        }
        
        if (textLanguageCode != null)  {
        	textLanguageCode.setClickable(false);
        	textLanguageCode.setFocusable(false);
        	textLanguageCode.setFocusableInTouchMode(false);
        	
        	textLanguageCode.setText( language.getCode() ); 
        }
	}
}
