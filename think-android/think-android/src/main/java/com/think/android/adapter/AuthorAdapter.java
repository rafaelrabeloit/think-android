package com.think.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import br.com.think.model.Author;
import com.think.android.view.AuthorItemView_;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.think.android.view.AuthorItemView;

@EBean
public class AuthorAdapter extends ModelAdapter<Author> {

	@RootContext
	Context context;
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		
		AuthorItemView authorItemView;
        if (convertView == null) {
        	authorItemView = AuthorItemView_.build(context);
        } else {
        	authorItemView = (AuthorItemView) convertView;
        }

        Author author = getItem(position);
        
        authorItemView.bind( author );

        return authorItemView;
        
    }
}