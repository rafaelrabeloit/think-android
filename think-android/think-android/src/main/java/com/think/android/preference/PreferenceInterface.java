package com.think.android.preference;

import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultInt;
import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultLong;
import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultString;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref.Scope;
import com.think.android.R;

@SharedPref(value=Scope.UNIQUE)
public interface PreferenceInterface {

	@DefaultString("")
	String username();
	
	@DefaultString("")
	String password();
	
	@DefaultLong(-1)
	long deviceId();

	@DefaultLong(-1)
	long languageId();

	@DefaultString("")
	String languageCode();
	
	@DefaultLong(-1)
	long userId();

	@DefaultLong(0)
	long lastUpdate();

	@DefaultInt(UserConfiguration.THEME_LIGHT)
	int theme();

	@DefaultInt(6)
	int updateTime();
	
	@DefaultBoolean(true)
	boolean updateEnable();

	@DefaultInt(R.color.blue_think)
	int quoteColor();
}
