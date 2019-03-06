package com.think.android.service;

import org.springframework.web.client.RestClientException;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import br.com.think.model.UserQuote;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EService;
import com.think.android.model.QuoteColor;
import com.think.android.preference.UserConfiguration;
import com.think.android.rest.ServiceClient;
import com.think.android.widget.WidgetHelper;

@EService
public class PeriodicUpdateService extends Service {

	private static final String TAG = "PeriodicUpdateService";

	private final IBinder mBinder = new UpdateServiceBinder();
	  
	@Bean
	ServiceClient client;
	
	@Bean
	UserConfiguration config;

	/**
	 * Dummy listener implementation for when it has no other attached.
	 */
	private static OnUpdateServiceListener listener = new OnUpdateServiceListener() {

		@Override
		public void onNewItem(Object newItem, Object aux) {	}
		
	};
	
	private OnUpdateServiceListener mListener = listener;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand");
		
		doRequest();
		
		return Service.START_NOT_STICKY;
	}
	  	
	@Background
	void doRequest(){

		try{
			//check if it is the first run
			client.checkFirstRun();
			
			UserQuote userQuote = client.getRandomUserQuote(config.getLanguageId(), config.getUserId());
			QuoteColor quoteColor = new QuoteColor(userQuote.getId(), config.getQuoteColor());
			mListener.onNewItem(userQuote, quoteColor);
			
			WidgetHelper.updateRecentQuotesWidgets(getApplicationContext());
		}
		catch(RestClientException e){
			//TODO error handling
			Log.d(TAG, "doRequest", e);
		}
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	public OnUpdateServiceListener getOnUpdateServiceListener() {
		return mListener;
	}

	public void setOnUpdateServiceListener(OnUpdateServiceListener mListener) {
		this.mListener = mListener;
	}
	
	public class UpdateServiceBinder extends Binder {
		public PeriodicUpdateService getService() {
			return PeriodicUpdateService.this;
		}
	}
	
	public interface OnUpdateServiceListener {
		public void onNewItem(Object newItem, Object aux);
	}
	
}