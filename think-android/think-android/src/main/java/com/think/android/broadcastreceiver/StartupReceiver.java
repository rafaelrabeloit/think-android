package com.think.android.broadcastreceiver;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.think.android.broadcastreceiver.StartUpdateServiceReceiver_;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EReceiver;
import com.googlecode.androidannotations.annotations.SystemService;
import com.think.android.activity.ConfigurationActivity;
import com.think.android.preference.UserConfiguration;

/**
 * It must be called only once per boot. If there was an `update`, then the current Alarm must be canceled in order to prevent multiples 
 * @author Rafael
 *
 */
@EReceiver
public class StartupReceiver extends BroadcastReceiver {

	private static final String TAG = "StartupReceiver";
	
	@Bean
	UserConfiguration config;
	
	@SystemService
	AlarmManager alarmService;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onReceive");
		
		Intent startIntent = new Intent(context, StartUpdateServiceReceiver_.class);
	    PendingIntent pending;
	    
		if(intent.getExtras() != null && intent.getExtras().containsKey(ConfigurationActivity.CHANGING_UPDATE_TIME) ){
			Log.d(TAG, "onReceive - Update");
			
			pending = PendingIntent.getBroadcast(context, 0, startIntent, 
			        PendingIntent.FLAG_UPDATE_CURRENT);
		}
		else{
			Log.d(TAG, "onReceive - Cancel Current");
			
			pending = PendingIntent.getBroadcast(context, 0, startIntent,
		        PendingIntent.FLAG_CANCEL_CURRENT);
		}
		
	    Calendar cal = Calendar.getInstance();
	    
	    // Start 1 second after boot completed
	    cal.add(Calendar.SECOND, 5);
	    
	    // InexactRepeating allows Android to optimize the energy consumption
	    alarmService.setInexactRepeating(AlarmManager.RTC_WAKEUP,
	        cal.getTimeInMillis(), 
	        config.getUpdateTime() /*Horas*/ * 60 /*Minutos*/ * 60 /*Segundos*/ * 1000 /*Milis*/, 
	        //30000,
	        pending);

	}
}