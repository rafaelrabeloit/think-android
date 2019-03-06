package com.think.android.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.think.model.Author;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.think.android.R;
import com.think.android.preference.UserConfiguration;

@EViewGroup(R.layout.item_view_author)
public class AuthorItemView extends LinearLayout {
	
	@Bean
	UserConfiguration config;
	
	@ViewById(R.id.text_author_fullName)
	TextView textAuthorFullName;
	
	public AuthorItemView(Context context) {
	    super(context);
	}

	/**
	 */
	public void bind(Author author) {
		        
        if (textAuthorFullName != null)  {
        	textAuthorFullName.setClickable(false);
        	textAuthorFullName.setFocusable(false);
        	textAuthorFullName.setFocusableInTouchMode(false);
        	
        	textAuthorFullName.setText( author.getFullName() ); 
        	textAuthorFullName.setTextColor( this.getContext().getResources().getColor(config.getForegroundColor()) );
        }

	}
}
