package com.think.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;
import com.think.android.activity.ConfigurationActivity_;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.ViewById;
import com.think.android.R;
import com.think.android.preference.UserConfiguration;

@EActivity(R.layout.activity_about)
public class AboutActivity extends Activity {

	public static final String TAG = "THINK";

	@Bean
	UserConfiguration config;
    
    @ViewById(R.id.text_about_title)
    TextView textTitle;

    @ViewById(R.id.text_about_team)
    TextView textTeam;

    @ViewById(R.id.text_about_greetings)
    TextView textGreetings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		config.changeTheme(this);
		
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
    }
    
    @OptionsItem(android.R.id.home)
    void home(){
        ConfigurationActivity_.intent(this)
    	.flags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
    	.start();
    
        finish();
    }

    @AfterViews
    public void init(){

		//Typeface for Custom Font
    	Typeface tf = Typeface.createFromAsset( getAssets(),"fonts/GiroLight.otf");
		textTitle.setTypeface(tf,Typeface.NORMAL);
		textTeam.setTypeface(tf,Typeface.NORMAL);
		textGreetings.setTypeface(tf,Typeface.NORMAL);
    }
}