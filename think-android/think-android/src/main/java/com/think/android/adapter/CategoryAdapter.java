package com.think.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import br.com.think.model.Category;
import com.think.android.view.CategoryItemView_;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.think.android.view.CategoryItemView;

@EBean
public class CategoryAdapter extends ModelAdapter<Category> {

	@RootContext
	Context context;
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		
		CategoryItemView categoryItemView;
        if (convertView == null) {
        	categoryItemView = CategoryItemView_.build(context);
        } else {
        	categoryItemView = (CategoryItemView) convertView;
        }

        Category category = getItem(position);
        
        categoryItemView.bind( category );

        return categoryItemView;
        
    }
}