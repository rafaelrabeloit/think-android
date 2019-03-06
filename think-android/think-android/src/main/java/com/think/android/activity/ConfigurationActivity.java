package com.think.android.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import com.think.android.activity.AboutActivity_;
import com.think.android.activity.ConfigurationActivity_;
import com.think.android.activity.MainActivity_;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.think.android.R;
import com.think.android.application.ThinkDataSource;
import com.think.android.dialog.LanguageDialogFragment;
import com.think.android.dialog.QuoteColorDialogFragment;
import com.think.android.dialog.ThemeDialogFragment;
import com.think.android.dialog.UpdateDialogFragment;
import com.think.android.dialog.LanguageDialogFragment.LanguageDialogListener;
import com.think.android.dialog.ThemeDialogFragment.ThemeDialogListener;
import com.think.android.dialog.UpdateDialogFragment.UpdateDialogListener;
import com.think.android.preference.UserConfiguration;
import com.think.android.widget.WidgetHelper;

@EActivity(R.layout.activity_configuration)
public class ConfigurationActivity extends Activity 
	implements QuoteColorDialogFragment.QuoteColorDialogListener, ThemeDialogListener, UpdateDialogListener, LanguageDialogListener {

	public static final String TAG = "ConfigurationActivity";

	public static final String CHANGING_UPDATE_TIME = "changing_time_update";
	
    @Bean
    UserConfiguration config;
	
    @Bean
    ThinkDataSource dataSource;
    
    @ViewById(R.id.switch_update)
    Switch switchUpdate;
    
    @ViewById(R.id.text_actual_update)
    TextView textActualUpdate;

    @ViewById(R.id.text_actual_theme)
    TextView textActualTheme;

    @ViewById(R.id.text_actual_color_quote)
    TextView textActualColorQuote;

    @ViewById(R.id.text_actual_language)
    TextView textActualLanguage;

    @ViewById(R.id.progress_bar_language)
    ProgressBar progressBarLanguage;
    
    @StringRes(R.string.message_actual_update)
    String messageActualUpdate;

    @StringRes(R.string.message_light)
    String messageLight;

    @StringRes(R.string.message_dark)
    String messageDark;

    @StringRes(R.string.message_blue)
    String messageBlue;
    @StringRes(R.string.message_green)
    String messageGreen;
    @StringRes(R.string.message_red)
    String messageRed;
    @StringRes(R.string.message_purple)
    String messagePurple;
    @StringRes(R.string.message_yellow)
    String messageYellow;
    @StringRes(R.string.message_orange)
    String messageOrange;
    @StringRes(R.string.message_transparent)
    String messageTransparent;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

		config.changeTheme(this);
		
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
    }
    
    @AfterViews
    public void loadConfig(){
    	loadIsUpdate();
    	
    	loadUpdate();
    	
    	loadColor();
    	
    	loadTheme();
    	
    	loadLanguage();
    }

    private void loadLanguage() {
    	
    	textActualLanguage.setText( config.getLanguageCode() );
	}
    
    private void loadUpdate() {
    	textActualUpdate.setText( messageActualUpdate.replace("1", String.valueOf(config.getUpdateTime() ) ));
	}

	private void loadTheme() {
    	if (config.getTheme()==UserConfiguration.THEME_DARK) {
			textActualTheme.setText(messageDark);
		} else {
			textActualTheme.setText(messageLight);
		}    	
	}

	private void loadIsUpdate() {
		switchUpdate.setActivated(config.isUpdateEnable());
	}

	public void loadColor(){

    	switch (config.getQuoteColor()) {
		case R.color.blue_think:
			textActualColorQuote.setText(messageBlue);
			break;

		case R.color.green_think:
			textActualColorQuote.setText(messageGreen);
			break;
			
		case R.color.red_think:
			textActualColorQuote.setText(messageRed);
			break;

		case R.color.yellow_think:
			textActualColorQuote.setText(messageYellow);
			break;

		case R.color.orange_think:
			textActualColorQuote.setText(messageOrange);
			break;

		case R.color.purple_think:
			textActualColorQuote.setText(messagePurple);
			break;

		case android.R.color.transparent:
			textActualColorQuote.setText(messageTransparent);
			break;
		default:
			break;
		}
    }
    
    public void click_textPolicy(View v){
    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://thinkapp.me/privacy"));
    	startActivity(browserIntent);
    }

    public void click_textAboutThink(View v){
		AboutActivity_.intent(this).start();
    }

    public void click_textThemes(View v){ 
    	DialogFragment newFragment = new ThemeDialogFragment(config.getTheme());
    	newFragment.show(getFragmentManager(), "themes");
    }

    public void click_textUpdates(View v){
    	DialogFragment newFragment = new UpdateDialogFragment();
    	newFragment.show(getFragmentManager(), "updates");
    }

    public void click_textColorQuotes(View v){
    	DialogFragment newFragment = new QuoteColorDialogFragment(config.getQuoteColor());
    	newFragment.show(getFragmentManager(), "colors");
    }

    public void click_textLanguages(View v){
    	DialogFragment newFragment = new LanguageDialogFragment();
    	newFragment.show(getFragmentManager(), "languages");
    }
    
    public void click_switchUpdate(View view) {
        // Is the toggle on?
        boolean on = ((Switch) view).isChecked();
        config.setUpdateEnable(on);        
    }

    @OptionsItem(android.R.id.home)
    void home() {     

        MainActivity_.intent(this)
        	.flags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
        	.start();
        
        finish();
    }

	@Override
	public void onQuoteColorDialogPositiveClick(DialogInterface dialog, int quoteColor,
			boolean all) {
		
		config.setQuoteColor(quoteColor);
		if(all){
			dataSource.changeAllColors(quoteColor);
		}
		
		loadColor();
	}

	@Override
	public void onUpdateDialogPositiveClick(DialogInterface dialog,
			int updateTime) {
		
		try{
			int update =  Integer.parseInt( getResources().getStringArray(R.array.array_string_update_time)[updateTime] );
			config.setUpdateTime( update );
			

			Intent intent = new Intent("br.com.think.broadcastreceiver.StartupReceiver_"); 
			intent.putExtra(CHANGING_UPDATE_TIME, true);
			sendBroadcast(intent);
			
			
			loadUpdate();
		}
		catch(NumberFormatException ex){ }
	}

	@Override
	public void onThemeDialogPositiveClick(DialogInterface dialog, int theme) {
		config.setTheme(theme);	
		
		WidgetHelper.updateAllWidgets(this.getApplicationContext());
		
		loadTheme();
		
		finish();
		ConfigurationActivity_.intent(this).start();		
	}

	@Override
	public void onLanguageDialogPositiveClick(DialogInterface dialog,
			int languageIndex) {
		// TODO Auto-generated method stub
		
	}

}

