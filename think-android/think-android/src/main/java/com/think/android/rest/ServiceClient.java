package com.think.android.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import br.com.think.model.Language;
import br.com.think.model.User;
import br.com.think.model.UserQuote;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.rest.RestService;
import com.think.android.interceptor.AuthHttpInterceptor;
import com.think.android.interceptor.SignatureHttpInterceptor;
import com.think.android.preference.UserConfiguration;

//TODO make the encapsulated methods
/**
 * Encapsulate the Request using Preference User Settings. The methods must throw RestClientException when its convenient
 * @author Rafael
 *
 */
@EBean
public class ServiceClient {

	private static String TAG = "ServiceClient";

	@RootContext
	Context context;

	@Bean
	UserConfiguration config;
	
    @RestService
	RestInterface rest;
    
	@Bean
    SignatureHttpInterceptor signInterceptor;    

    @Bean
    AuthHttpInterceptor authInterceptor;

    List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
    
	@AfterInject
	public void init(){
        rest.setRootUrl(config.getEndPoint());
	}
	
    void initIntercept(boolean auth) {
    	
        RestTemplate template = rest.getRestTemplate();   

        interceptors.clear();

        interceptors.add(signInterceptor);
        if (auth) interceptors.add(authInterceptor);

        template.setInterceptors(interceptors);        
    }
    
	public Language getLanguage(String code) {
		
		initIntercept(false);
		return rest.getLanguage(code);		
	}
	
	public User addUser(User user) {
		
		initIntercept(false);
		return rest.addUser(user);		
	}
	
	public UserQuote[] getRecentsUserQuotes(long languageId, long userId,
			long lastUpdate) {

		initIntercept(true);
		return rest.getRecentsUserQuotes(languageId, userId, lastUpdate);
	}

	public UserQuote getRandomUserQuote(long languageId, long userId) {
		
		initIntercept(true);
		return rest.getRandomUserQuote(languageId, userId);
	}
	
	/**
	 * 
	 * @return true if the is first run and a new user was registered. false if it was not the first run. throws RestClientException if it can't register the user.
	 */
	public boolean checkFirstRun() throws RestClientException {
		Log.d(TAG, "firstRun");
		
		//Is first run
		if(config.getUsername().equals("")){
			
			String locale = Locale.getDefault().toString();
			
			Language lang = null;
			
			try {
				lang = getLanguage(locale);
				
			} catch (Exception e) {
				//Default fallback
				lang = new Language("en_US", "US English");
				lang.setId(1);
			}

			//Register user on Server
			User user = addUser( new User(lang, null, "basic", null, null, 0) );
			
			//Register user on Phone
			config.setUsername(user.getNickname());
			config.setPassword(user.getPassword());
			config.setUserId(user.getId());
			config.setLanguageId(lang.getId());
			config.setLanguageCode(lang.getCode());
			
			/*
			config.setUsername("laismm");
			config.setPassword("f5ddd99d-77c1-4c31-bbc5-c10e854ae517");
			config.setUserId(2);
			*/
			
			//TODO Register the Device
			//Device device = restClient.addDevice( new Device(), lang.getId(), safeStore.getUserId() );

			//Start Services
			Intent intent = new Intent("br.com.think.broadcastreceiver.StartupReceiver_"); 
			context.sendBroadcast(intent);

			return true;
		}
		else{
			return false;
		}
		
	}
    
}
