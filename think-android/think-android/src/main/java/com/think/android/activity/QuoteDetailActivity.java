package com.think.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import com.think.android.activity.MainActivity_;
import com.think.android.fragment.QuoteDetailFragment_;
import br.com.think.model.Quote;
import br.com.think.model.UserQuote;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.think.android.R;
import com.think.android.application.ThinkDataSource;
import com.think.android.fragment.QuoteDetailFragment;
import com.think.android.preference.UserConfiguration;

/**
 * An activity representing a single Item detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link ItemListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link ItemDetailFragment}.
 */
@EActivity(R.layout.activity_quote_detail)
@OptionsMenu(R.menu.detail)
public class QuoteDetailActivity extends Activity {

	public static final String TAG = "THINK";
	
	@Bean
	UserConfiguration config;
	
	@Bean
	ThinkDataSource dataSource;
	
	@Extra
	String quoteId;
	
	@StringRes(R.string.text_share_subject)
	String shareSubject;
		
	MenuItem menuFavorite;
	MenuItem menuUnfavorite;
	MenuItem menuShare;
	
	QuoteDetailFragment fragment;
	
	UserQuote userQuote;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Set the Theme according to configurations
		config.changeTheme(this);
		
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.	
			fragment = QuoteDetailFragment_.builder()
					  .quoteId(quoteId)
					  .build();

			getFragmentManager().beginTransaction()
					.add(R.id.quote_detail_container, fragment).commit();
		}
	}

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        
        this.menuFavorite = menu.findItem(R.id.menu_favorite);
        this.menuUnfavorite = menu.findItem(R.id.menu_unfavorite);
        this.menuShare = menu.findItem(R.id.menu_share);
        
        this.userQuote = fragment.getUserQuote();
        setUserQuote(userQuote);
        
        return true;
    }
    
    /**
     * 
     * @param userQuote
     */
    public void setUserQuote(UserQuote userQuote){

    	//Construct the Share Intent
    	Intent sharingIntent = new Intent(Intent.ACTION_SEND);	           	            
        sharingIntent.setType("text/plain");
        
        //Get the Quote text for Share intent
        Quote quote = (Quote) userQuote.getRootQuote().getQuotes().toArray()[0];
    	
        String shareBody = shareSubject + " - " + quote.getAuthorFullname() + '\n' + quote.getText();
        
        //sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, );
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

        // Get the provider and hold onto it to set/change the share intent.
        ShareActionProvider mShareActionProvider = (ShareActionProvider) menuShare.getActionProvider();
        mShareActionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        
        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        mShareActionProvider.setShareIntent(sharingIntent);
        
        //Set the favorite button
        if(userQuote.isFavorited()){
        	menuFavorite.setVisible(false);
        	menuUnfavorite.setVisible(true);        	
        }
        else{
        	menuFavorite.setVisible(true);
        	menuUnfavorite.setVisible(false);  
        }
    }
    
    @OptionsItem(android.R.id.home)
    void home() {     
    	
        MainActivity_.intent(this)
        	.flags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
        	.start();
        
        finish();
    }
    
    @OptionsItem(R.id.menu_favorite)
    void favorite(){
    	menuFavorite.setVisible(false);
    	menuUnfavorite.setVisible(true); 
    	
    	dataSource.setFavorite(userQuote, true);
    }

    @OptionsItem(R.id.menu_unfavorite)
    void unfavorite(){        
    	menuFavorite.setVisible(true);
    	menuUnfavorite.setVisible(false);
 
    	
    	dataSource.setFavorite(userQuote, false);
    }
    
}
