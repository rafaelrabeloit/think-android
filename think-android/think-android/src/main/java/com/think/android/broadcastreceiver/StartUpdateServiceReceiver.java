package com.think.android.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.think.android.service.PeriodicUpdateService_;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EReceiver;
import com.think.android.preference.UserConfiguration;

@EReceiver
public class StartUpdateServiceReceiver extends BroadcastReceiver {

	private static final String TAG = "StartUpdateServiceReceiver";
	
	@Bean
	UserConfiguration config;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onReceive");
		
		if(config.isUpdateEnable())
			PeriodicUpdateService_.intent(context).start();
		
	}
}