package com.think.android.preference;

import java.io.IOException;
import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.util.Log;
import com.think.android.preference.PreferenceInterface_;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.googlecode.androidannotations.api.Scope;
import com.think.android.R;

@EBean(scope = Scope.Singleton)
public class UserConfiguration {

	private static String TAG = "UserConfiguration";
	public static final int THEME_LIGHT = 0;
	public static final int THEME_DARK = 1;
	
	private static String apiKey = "";
    private static String privateKey = "";
    private static String endPoint = "";
    
	private String username;
	private String password;
	private String languageCode;
	private long deviceId;
	private long languageId;
	private long userId;
	private int theme;	
	private int updateTime;	
	private boolean updateEnable;	
	private int quoteColor;
	private long lastUpdate;
	
	@RootContext
	Context context;
	
	@Pref
    PreferenceInterface_ preferences ;

    /**
     * Restore preferences   
     */
	@AfterInject
    public void init() {
     
        this.username = preferences.username().get();

        this.password = decrypt( preferences.password().get() );

        this.userId = preferences.userId().get();
        
        this.theme = preferences.theme().get();
        
        this.deviceId = preferences.deviceId().get();
        
        this.languageId = preferences.languageId().get();
        
        this.updateTime = preferences.updateTime().get();
        
        this.updateEnable = preferences.updateEnable().get();
        
        this.quoteColor = preferences.quoteColor().get();
        
        this.lastUpdate = preferences.lastUpdate().get();
        
        //Try to read the config file
        try {
            Properties properties = new Properties();
			properties.load( context.getResources().openRawResource(R.raw.config) );
			
			apiKey = properties.getProperty("think.apiKey");
			privateKey = properties.getProperty("think.privateKey");
			endPoint = properties.getProperty("think.endPoint");	   
			
		} catch (NotFoundException e) {
			e.printStackTrace();
			Log.wtf(TAG, e);
		} catch (IOException e) {
			e.printStackTrace();
			Log.wtf(TAG, e);
		}
    }

	public int getTheme() {
		return theme;
	}

	public void setTheme(int theme) {
		this.theme = theme;
		
		preferences.edit().theme().put(theme).apply();
	}

	public boolean isUpdateEnable() {
		return updateEnable;
	}

	public void setUpdateEnable(boolean updateEnable) {
		this.updateEnable = updateEnable;

		preferences.edit().updateEnable().put(updateEnable).apply();
	}
	
	public int getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(int updateTime) {
		this.updateTime = updateTime;

		preferences.edit().updateTime().put(updateTime).apply();
	}

	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;

		preferences.edit().deviceId().put(deviceId).apply();
	}
	
    public long getDeviceId() {
      return deviceId;
    }
    
	public void setLanguageId(long languageId) {
		this.languageId = languageId;

		preferences.edit().languageId().put(languageId).apply();
	}
	
    public long getLanguageId() {
      return languageId;
    }

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;

		preferences.edit().languageCode().put(languageCode).apply();
	}
	
    public String getLanguageCode() {
      return languageCode;
    }
    
	public long getUserId() {
		return userId;
	}
	
	public void setUserId(long userId) {
		this.userId = userId;

		preferences.edit().userId().put(userId).apply();
	}
	
    public String getUsername() {
      return username;
    }
    
	public void setUsername(String username) {
		this.username = username;

		preferences.edit().username().put(username).apply();
	}
    
    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
		this.password = encrypt(password);

		preferences.edit().password().put(password).apply();
	}

    public int getQuoteColor() {
      return quoteColor;
    }

    public void setQuoteColor(int quoteColor) {
		this.quoteColor = quoteColor;
		
		preferences.edit().quoteColor().put(quoteColor).apply();
	}

	public long getLastUpdate() {
		return lastUpdate;
	}
	
	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
		
		preferences.edit().lastUpdate().put(lastUpdate).apply();
	}
    
    //Aux

	public void changeTheme(Activity target) {
		if(theme==THEME_LIGHT){
			target.setTheme(R.style.Theme_Think_Light);
		}
		else{
			target.setTheme(R.style.Theme_Think_Dark);
		}
	}
	
    private String encrypt(String str){
    	return str;
    }
    
    private String decrypt(String str){
    	return str;
    }
    
    public String getApiKey() {
        return apiKey;
    }

    public String getPrivateKey() {
      return privateKey;
    }
    
    public String getEndPoint() {
      return endPoint;
    }

	public int getForegroundColor() {
		if(this.theme == UserConfiguration.THEME_LIGHT)
			return R.color.foreground_light;
		else 
			return R.color.foreground_dark;
	}

	public int getFavoriteButton() {
		if(this.theme == UserConfiguration.THEME_LIGHT)
			return R.drawable.ic_menu_favorite_light;
		else 
			return R.drawable.ic_menu_favorite_dark;
	}

	public int getShareButton() {
		if(this.theme == UserConfiguration.THEME_LIGHT)
			return R.drawable.ic_menu_share_light;
		else 
			return R.drawable.ic_menu_share_dark;
	}

	public int getBackgroundTabColor() {
		if(this.theme == UserConfiguration.THEME_LIGHT)
			return R.color.background_tab_light;
		else 
			return R.color.background_tab_dark;
	}
	
	public int getQuoteMarkLeftImage() {
		if(this.theme == UserConfiguration.THEME_LIGHT)
			return R.drawable.quote_mark_left_light;
		else 
			return R.drawable.quote_mark_left_dark;
	}

	public int getQuoteMarkRightImage() {
		if(this.theme == UserConfiguration.THEME_LIGHT)
			return R.drawable.quote_mark_right_light;
		else 
			return R.drawable.quote_mark_right_dark;
	}

	public int getBackgroundWidgetColor() {
		if(this.theme == UserConfiguration.THEME_LIGHT)
			return R.color.background_light_transparent;
		else 
			return R.color.background_dark_transparent;
	}

}