package com.think.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import br.com.think.model.Language;
import com.think.android.view.LanguageItemView_;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.think.android.view.LanguageItemView;

@EBean
public class LanguageAdapter extends ModelAdapter<Language> {

	@RootContext
	Context context;
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		
		LanguageItemView languageItemView;
        if (convertView == null) {
        	languageItemView = LanguageItemView_.build(context);
        } else {
        	languageItemView = (LanguageItemView) convertView;
        }

        Language language = getItem(position);
        
        languageItemView.bind( language );

        return languageItemView;
        
    }
}