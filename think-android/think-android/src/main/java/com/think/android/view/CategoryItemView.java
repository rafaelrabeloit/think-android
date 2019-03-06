package com.think.android.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.think.model.Category;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.think.android.R;
import com.think.android.preference.UserConfiguration;

@EViewGroup(R.layout.item_view_category)
public class CategoryItemView extends LinearLayout {
	
	@Bean
	UserConfiguration config;
	
	@ViewById(R.id.text_category_description)
	TextView textCategoryDescription;
	
	public CategoryItemView(Context context) {
	    super(context);
	}

	/**
	 * 
	 */
	public void bind(Category category) {
		        
        if (textCategoryDescription != null)  {
        	textCategoryDescription.setClickable(false);
        	textCategoryDescription.setFocusable(false);
        	textCategoryDescription.setFocusableInTouchMode(false);
        	
        	textCategoryDescription.setText( category.getDescription() ); 
        	textCategoryDescription.setTextColor( this.getContext().getResources().getColor(config.getForegroundColor()) );
        }

	}
}
