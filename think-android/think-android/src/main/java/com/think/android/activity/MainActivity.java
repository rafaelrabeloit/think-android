package com.think.android.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import com.think.android.activity.ConfigurationActivity_;
import com.think.android.activity.QuoteDetailActivity_;
import com.think.android.fragment.AuthorQuotesFragment_;
import com.think.android.fragment.AuthorsFragment_;
import com.think.android.fragment.CategoriesFragment_;
import com.think.android.fragment.CategoryQuotesFragment_;
import com.think.android.fragment.FavoriteQuotesFragment_;
import com.think.android.fragment.QuoteDetailFragment_;
import com.think.android.fragment.RecentQuotesFragment_;
import com.think.android.fragment.SearchResultQuotesFragment_;
import br.com.think.model.Quote;
import br.com.think.model.UserQuote;
import com.think.android.service.PeriodicUpdateService_;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.think.android.R;
import com.think.android.application.ThinkDataSource;
import com.think.android.fragment.AuthorQuotesFragment;
import com.think.android.fragment.CategoryQuotesFragment;
import com.think.android.fragment.ItemListFragment;
import com.think.android.fragment.QuoteDetailFragment;
import com.think.android.fragment.SearchResultQuotesFragment;
import com.think.android.model.QuoteColor;
import com.think.android.preference.UserConfiguration;
import com.think.android.service.PeriodicUpdateService;
import com.think.android.util.TypefaceSpan;

@EActivity(R.layout.activity_main_onepanel)
@OptionsMenu(R.menu.main)
public class MainActivity extends Activity 
	implements ItemListFragment.Callbacks, PeriodicUpdateService.OnUpdateServiceListener {

	public static final String TAG = "MainActivity";
    
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	SearchResultQuotesFragment searchQuotes = null;
	
	AuthorQuotesFragment authorQuotes = null;
	
	CategoryQuotesFragment categoryQuotes = null;
	
	@StringRes
	String appName;
	
    @Bean
    UserConfiguration config;
    
    @Bean
    ThinkDataSource dataSource;
    
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	
	private PeriodicUpdateService s;

	MainActivity instance = this;
    
    private OnClickListener shareClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int id = (Integer) v.getTag();
	        if (id >= 0) {
	            UserQuote uq = dataSource.getUserQuoteList().get(id);
	            
	            Intent sharingIntent = new Intent(Intent.ACTION_SEND);	           	            
	            sharingIntent.setType("text/plain");
	            sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            
	            Quote quote = (Quote) uq.getRootQuote().getQuotes().toArray()[0];
	        	
	            String shareBody = quote.getText();
	            
	            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, 
	            		instance.getResources().getString(R.string.text_share_subject) + " - " + quote.getAuthorFullname());
	            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

	            
	            instance.startActivity(
	            		Intent.createChooser(sharingIntent, instance.getResources().getString(R.string.text_share_title) )
	            		);
	        }
		}
	};

	private OnClickListener favoriteClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int id = (Integer) v.getTag();
	        if (id > 0) {
	            UserQuote uq = dataSource.getUserQuoteList().get(id);
	            
	            if(uq.isFavorited()){
	            	dataSource.setFavorite(uq, false);
	            	((ImageButton) v ).setImageResource(config.getFavoriteButton());
	            }
	            else{
	            	dataSource.setFavorite(uq, true);
	            	((ImageButton) v ).setImageResource( R.drawable.favorite_active );
	            }
	        }
		}
	};
    
	private ServiceConnection mConnection = new ServiceConnection() {

	    public void onServiceConnected(ComponentName className, IBinder binder) {
	      s = ((PeriodicUpdateService.UpdateServiceBinder) binder).getService();
	      ((PeriodicUpdateService) s).setOnUpdateServiceListener(instance);
	    }

	    public void onServiceDisconnected(ComponentName className) {
	      s = null;
	    }
	};
	
    public boolean isTwoPane() {
		return mTwoPane;
	}

	/**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after 
     * previously being shut down then this Bundle contains the data it most 
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

		//Set the Theme according to configurations
		config.changeTheme(this);
		
		// setup action bar for tabs
	    ActionBar actionBar = getActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	    actionBar.setDisplayShowTitleEnabled(true);
	    	    
	    //Fix the Launcher Logo on Search Action Provider. No. use setDisplayUseLogoEnabled doesn't work.
	    actionBar.setIcon(R.drawable.ic_logo);
	    
	    //set the title to the logo and name
	    setDefaultTitle();
	    
	    // Get the intent, verify the action and get the query
	    Intent intent = getIntent();
	    handleIntent(intent);
	    
	    Tab tab = actionBar.newTab()
	            .setText(R.string.recents)
	            .setTabListener(new TabListener<RecentQuotesFragment_>(
	                    this, "list", RecentQuotesFragment_.class));
	    actionBar.addTab(tab);

	    tab = actionBar.newTab()
	        .setText(R.string.favorites)
	        .setTabListener(new TabListener<FavoriteQuotesFragment_>(
	                this, "list", FavoriteQuotesFragment_.class));
	    actionBar.addTab(tab);
	            
	    tab = actionBar.newTab()
	        .setText(R.string.authors)
	        .setTabListener(new TabListener<AuthorsFragment_>(
	                this, "list", AuthorsFragment_.class));
	    actionBar.addTab(tab);
	    
	    tab = actionBar.newTab()
	        .setText(R.string.categories)
	        .setTabListener(new TabListener<CategoriesFragment_>(
	                this, "list", CategoriesFragment_.class));
	    actionBar.addTab(tab);
	 
	    
	    dataSource.getRecentUserQuoteAdapter().setFavoriteClickListener(favoriteClick);
	    dataSource.getRecentUserQuoteAdapter().setShareClickListener(shareClick);

	    dataSource.getAuxUserQuoteAdapter().setFavoriteClickListener(favoriteClick);
	    dataSource.getAuxUserQuoteAdapter().setShareClickListener(shareClick);
	    	    
	    
	    
	    bindService();
    }

    @Override
    protected void onRestart() {
    	Log.d(TAG, "onRestart");
    	bindService();
    	super.onRestart();
    }
    
    @Override
    protected void onStop() {
    	Log.d(TAG, "onStop");
    	unbindService(mConnection);
    	super.onStop();
    }

    private void bindService() {
    	Log.d(TAG, "bindService");
    	bindService(PeriodicUpdateService_.intent(this).get(), mConnection,
            Context.BIND_AUTO_CREATE);		
	}

	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	Log.d(TAG, "onPrepareOptionsMenu");
        super.onPrepareOptionsMenu(menu);
        
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenu = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchMenu.getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        
        searchMenu.setOnActionExpandListener(new OnActionExpandListener() {
        	private final static String TAG = "OnActionExpandListener";
        	
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item){
            	Log.d(TAG, "onMenuItemActionCollapse");
            	
            	getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            	if(searchQuotes!=null)
            		getFragmentManager().beginTransaction()
            			.detach(searchQuotes).commit();
            	
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item){
                return true;
            }
        });
        
        return true;
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
    	Log.d(TAG, "onNewIntent");
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
    	Log.d(TAG, "handleIntent - search");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
  	    	search(query);
        }
    }

	private void search(String query) {
    	Log.d(TAG, "search");
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		

		if (mTwoPane) 
			findViewById(R.id.quote_detail_container).setVisibility(View.VISIBLE);				
		
		findViewById(R.id.quote_list).setVisibility(View.VISIBLE);
		findViewById(R.id.groups_container).setVisibility(View.GONE);
		
		FragmentTransaction ft = getFragmentManager().beginTransaction();

		Fragment listGroup = getFragmentManager().findFragmentByTag("listGroup");
		if(listGroup!=null)
			ft.detach(listGroup);
		
		if(searchQuotes != null){
			searchQuotes.setSearch(query);
			searchQuotes.bindAdapter();
		
			if(searchQuotes.isDetached()) ft.attach(searchQuotes);
		}
		else
		{
			searchQuotes = SearchResultQuotesFragment_.builder()
				.search(query)
				.build();
			
			ft.add(R.id.quote_list, searchQuotes, "listSearch");
		}
		
    	ft.commit();

    	Log.d(TAG, "end search");	
	}

	@AfterViews
    void init(){
		if (findViewById(R.id.quote_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;
		}
		
    }

    @OptionsItem(R.id.menu_new)
    void newRandom() {     
    	dataSource.getNewRandomQuote();
    }

    @OptionsItem(R.id.menu_refresh)
    void refresh() {     
    	dataSource.getRecentsUserQuotes();
    }

    @OptionsItem(R.id.menu_random)
    void random() {     
    	UserQuote uq =  dataSource.getRandomUserQuote() ;
    	if(uq!=null)
    		onItemSelected(String.valueOf(uq.getId()));
    }
    
    @OptionsItem(R.id.menu_settings)
    void settings() {     

		ConfigurationActivity_.intent(this).start();
		finish();
    }
    
    @OptionsItem(android.R.id.home)
    void home() {     
		Log.d(TAG, "home");

    	setDefaultTitle();

		if ( getActionBar().getSelectedTab().getPosition() == 2 || getActionBar().getSelectedTab().getPosition() == 3 ) {
			
			Fragment listGroup = getFragmentManager().findFragmentByTag("listGroup");
			if(listGroup!=null)
				getFragmentManager().beginTransaction().detach(listGroup).commit();
			
			findViewById(R.id.groups_container).setVisibility(View.VISIBLE);
			
			findViewById(R.id.quote_list).setVisibility(View.GONE);			
			if(mTwoPane) findViewById(R.id.quote_detail_container).setVisibility(View.GONE);
			
		}

    }
    
    private void setCustomTitle(String s) {
   
	    getActionBar().setDisplayHomeAsUpEnabled(true);

	    getActionBar().setTitle(s);
	    
	    //Fix Margin and Size for the Title with Custom Font
	    try {
	    	final int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
	        TextView title = (TextView) getWindow().findViewById(titleId);
	        title.setPadding(0, 0, 0, 0);
	        
	        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (title.getTextSize() / 2) );

	    } catch (Exception e) {
	        Log.e(TAG, "Failed to obtain action bar title reference");
	    }
	    
	}
    
	private void setDefaultTitle() {

		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setHomeButtonEnabled(false);
		
	    //Apply Custom Font
	    SpannableString s = new SpannableString(appName);
	    s.setSpan(new TypefaceSpan(this, "GiroLight"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);	    
	    getActionBar().setTitle(s);
	    
	    //Fix MArgin and Size for the Title with Custom Font
	    try {
	    	final int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
	        TextView title = (TextView) getWindow().findViewById(titleId);
	        title.setPadding(0, 10, 0, 0);
	        
	        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (title.getTextSize() * 2) );

	    } catch (Exception e) {
	        Log.e(TAG, "Failed to obtain action bar title reference");
	    }
	    
	}

	/**
	 * Callback method from {@link ItemListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		Log.d(TAG, "onItemSelected");


		if (findViewById(R.id.groups_container).getVisibility() == View.VISIBLE){
			
			if (mTwoPane) 
				findViewById(R.id.quote_detail_container).setVisibility(View.VISIBLE);				
			
			findViewById(R.id.quote_list).setVisibility(View.VISIBLE);
			findViewById(R.id.groups_container).setVisibility(View.GONE);

			FragmentTransaction ft = getFragmentManager().beginTransaction();
			
			//Author
			if (this.getActionBar().getSelectedTab().getPosition()==2 ){
				
				if(authorQuotes != null){
					
					authorQuotes.setAuthorId(id);
					authorQuotes.bindAdapter();
					authorQuotes.getArrayAdapter().notifyDataSetChanged();
					
					ft.attach(authorQuotes);
					
				}
				else
				{
					authorQuotes = AuthorQuotesFragment_.builder()
							.authorId(id)
							.build();
					
					ft.add(R.id.quote_list, authorQuotes, "listGroup");
				}
				
								
				setCustomTitle( dataSource.getAuthorAdapter().getItem( dataSource.getAuthorAdapter().getPosition( Integer.parseInt(id)) ).getFullName() );
				
			}//Category
			else{
				
				if(categoryQuotes != null){
					categoryQuotes.setCategoryId(id);
					categoryQuotes.bindAdapter();
				
					ft.attach(categoryQuotes);
				}
				else
				{
					categoryQuotes = CategoryQuotesFragment_.builder()
							.categoryId(id)
							.build();
					
					ft.add(R.id.quote_list, categoryQuotes, "listGroup");
				}
				
				setCustomTitle( dataSource.getCategoryAdapter().getItem( dataSource.getCategoryAdapter().getPosition( Integer.parseInt(id)) ).getDescription() );
				
			}
			
			ft.commit();
		}
		else{

			if (mTwoPane) {
				// In two-pane mode, show the detail view in this activity by
				// adding or replacing the detail fragment using a
				// fragment transaction.
				QuoteDetailFragment fragment = QuoteDetailFragment_.builder()
					.quoteId(id)
					.build();
				
				getFragmentManager().beginTransaction()
					.replace(R.id.quote_detail_container, fragment, "quoteDetail").commit();
			}
			else{
				QuoteDetailActivity_.intent(this).quoteId(id).start();
			}
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.d(TAG, "onRestoreInstanceState");

		//Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}	
		
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.d(TAG, "onSaveInstanceState");
		
		//Serialize the current tab position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
			.getSelectedNavigationIndex());
		
		
	}

	
    private class TabListener<T extends Fragment> implements
			ActionBar.TabListener {
    	private final static String TAG = "TabListener";
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;
		private Fragment mFragment;
		
		/**
		 * Constructor used each time a new tab is created.
		 * 
		 * @param activity
		 *            The host Activity, used to instantiate the fragment
		 * @param tag
		 *            The identifier tag for the fragment
		 * @param clz
		 *            The fragment's Class, used to instantiate the fragment
		 */
		public TabListener(Activity activity, String tag, Class<T> clz) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
		}
		
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			Log.d(TAG, "onTabSelected");
			
			if(	(getActionBar().getDisplayOptions() & ActionBar.DISPLAY_HOME_AS_UP) != 0 ) setDefaultTitle();
			

			if(tab.getPosition() == 2 || tab.getPosition() == 3){

				findViewById(R.id.quote_list).setVisibility(View.GONE);				
				if(mTwoPane) findViewById(R.id.quote_detail_container).setVisibility(View.GONE);
				
				findViewById(R.id.groups_container).setVisibility(View.VISIBLE);
			}
			else{
			
				if(findViewById(R.id.quote_list)!=null && findViewById(R.id.quote_list).getVisibility()!=View.VISIBLE){
										
					findViewById(R.id.quote_list).setVisibility(View.VISIBLE);
					if(mTwoPane) findViewById(R.id.quote_detail_container).setVisibility(View.VISIBLE);

					findViewById(R.id.groups_container).setVisibility(View.GONE);
				
				}
			}
			
			// Check if the fragment is already initialized
			if (mFragment == null) {
				
				// If not, instantiate and add it to the activity
				mFragment = Fragment.instantiate(mActivity, mClass.getName());
				
				if(tab.getPosition() == 2 || tab.getPosition() == 3){
					
					//Replace the layout with this fragment
					ft.add(R.id.groups_container, mFragment, mTag);
					
				}
				else{
					
					//Replace the layout with this fragment
					ft.add(R.id.quote_list, mFragment, mTag);
					
				}

				
				
			} else {
				// If it exists, simply attach it in order to show it
				ft.attach(mFragment);
			}
						
		}
		
		
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			Log.d(TAG, "onTabUnselected");
			
			if (mFragment != null) {
				// Detach the fragment, because another one is being attached
				ft.detach(mFragment);
				
				//Get the 'list' of the group and remove!
				if(tab.getPosition() == 2 || tab.getPosition() == 3) {
					Fragment listGroup = getFragmentManager().findFragmentByTag("listGroup");
					if ( listGroup != null ) ft.detach( listGroup );
				}
				
				Fragment quoteDetail = getFragmentManager().findFragmentByTag("quoteDetail");
				if ( quoteDetail != null ) ft.remove( quoteDetail );
			}
			
		}
		
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// User selected the already selected tab. Usually do nothing.
		}
	}

	@Override
	public void onNewItem(Object newItem, Object aux) {
		UserQuote userQuote = (UserQuote) newItem;
		QuoteColor quoteColor = (QuoteColor) aux;
		dataSource.recent(userQuote, quoteColor);
	}

}

