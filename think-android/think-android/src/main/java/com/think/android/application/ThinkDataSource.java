package com.think.android.application;

import java.util.Random;

import org.springframework.web.client.RestClientException;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import br.com.think.model.Author;
import br.com.think.model.Category;
import br.com.think.model.Quote;
import br.com.think.model.UserQuote;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.api.Scope;
import com.think.android.adapter.AuthorAdapter;
import com.think.android.adapter.CategoryAdapter;
import com.think.android.adapter.UserQuoteAdapter;
import com.think.android.data.DataPersistence;
import com.think.android.model.QuoteColor;
import com.think.android.preference.UserConfiguration;
import com.think.android.rest.ServiceClient;
import com.think.android.util.ModelSparseArray;
import com.think.android.widget.WidgetHelper;

@EBean(scope = Scope.Singleton)
public class ThinkDataSource {

	private static String TAG = "ThinkDataSource";
	
	@RootContext
	Context context;

	@Bean
	UserConfiguration config;
	
	@Bean
	DataPersistence persist;
	
	@Bean
	ServiceClient client;
	
	SparseArray<UserQuote> userQuoteList;
	
	ModelSparseArray<QuoteColor> quoteColorsList;
	
	@Bean
	CategoryAdapter categoryAdapter;
	
	@Bean
	AuthorAdapter authorAdapter;

	@Bean
	UserQuoteAdapter favoriteUserQuoteAdapter;
	
	@Bean
	UserQuoteAdapter recentUserQuoteAdapter;

	@Bean
	UserQuoteAdapter auxUserQuoteAdapter;
	
	public CategoryAdapter getCategoryAdapter() {
		return categoryAdapter;
	}
	
	public AuthorAdapter getAuthorAdapter() {
		return authorAdapter;
	}

	public void setAuthorAdapter(AuthorAdapter authorAdapter) {
		this.authorAdapter = authorAdapter;
	}

	public UserQuoteAdapter getFavoriteUserQuoteAdapter() {
		return favoriteUserQuoteAdapter;
	}

	public void setFavoriteUserQuoteAdapter(
			UserQuoteAdapter favoriteUserQuoteAdapter) {
		this.favoriteUserQuoteAdapter = favoriteUserQuoteAdapter;
	}

	public UserQuoteAdapter getRecentUserQuoteAdapter() {
		return recentUserQuoteAdapter;
	}

	public void setRecentUserQuoteAdapter(UserQuoteAdapter recentUserQuoteAdapter) {
		this.recentUserQuoteAdapter = recentUserQuoteAdapter;
	}   

	public UserQuoteAdapter getAuxUserQuoteAdapter() {
		return auxUserQuoteAdapter;
	}

	public void setAuxUserQuoteAdapter(UserQuoteAdapter auxUserQuoteAdapter) {
		this.auxUserQuoteAdapter = auxUserQuoteAdapter;
	} 
	
	public SparseArray<UserQuote> getUserQuoteList() {
		return userQuoteList;
	}

	public void setUserQuoteList(SparseArray<UserQuote> userQuoteList) {
		this.userQuoteList = userQuoteList;
	}

	public ModelSparseArray<QuoteColor> getColorQuotes() {
		return quoteColorsList;
	}
	
	@AfterInject
	public void init() {
		Log.d(TAG, "init");
		
		this.userQuoteList = new SparseArray<UserQuote>();
		this.quoteColorsList = new ModelSparseArray<QuoteColor>();
		
		recentUserQuoteAdapter.setWide(true);
		recentUserQuoteAdapter.setColorQuotes( this.quoteColorsList );
		favoriteUserQuoteAdapter.setWide(false);
		favoriteUserQuoteAdapter.setColorQuotes( this.quoteColorsList );

		auxUserQuoteAdapter.setColorQuotes(quoteColorsList);
		
		load();		
	}
	
	@Background
	public void load() {
		Log.d(TAG, "load");
		
		this.quoteColorsList.addAll( persist.queryForAllQuoteColors() );
		
		//Add each User Quote to the Recent List, with a QuoteColor
		for(UserQuote userQuote : persist.queryForAllUserQuote()){
			QuoteColor quoteColor = this.quoteColorsList.getByKey(userQuote.getId());
			
			//Nunca deve acontecer
			if(quoteColor==null) {
				Log.wtf(TAG, "UserQuote without Color!");
				quoteColor = new QuoteColor(userQuote.getId(), config.getQuoteColor());
				persist.createQuoteColor(quoteColor);
			}
			
			if(userQuote.isFavorited()) favorite(userQuote, userQuote.isFavorited());
			recent(userQuote, quoteColor);
		}
				
		getRecentsUserQuotes();
	}
	
	@Background
	public void getRecentsUserQuotes() {
		Log.d(TAG, "getRecentsUserQuotes");

		try{
			
			//check if it is the first run
			client.checkFirstRun();
			
			UserQuote[] userQuotes = client.getRecentsUserQuotes(config.getLanguageId(), config.getUserId(), config.getLastUpdate() );
	
			//Tem qeu ser antes dos binds se nao a app nao reconhece a mudanca na cor
			for(UserQuote userQuote : userQuotes){
				QuoteColor quoteColor = new QuoteColor(userQuote.getId(), config.getQuoteColor());
				recent(userQuote, quoteColor);	
				
				persist.createUserQuote(userQuote);	
				persist.createQuoteColor(quoteColor);	
			}
			
			WidgetHelper.updateRecentQuotesWidgets(context);
			
			if(userQuotes.length > 0 )
				config.setLastUpdate(userQuotes[0].getReceivedOn().getTime());
		}
		catch (RestClientException e) {
			//TODO error handling
			Log.d(TAG, "getRecentsUserQuotes", e);
		}
		
	}

	@Background
	public void getNewRandomQuote() {
		try{
			//check if it is the first run
			client.checkFirstRun();
			
			UserQuote userQuote = client.getRandomUserQuote(config.getLanguageId(), config.getUserId());
	
			//TODO make tests later
			if(userQuote == null) return;
			
			QuoteColor quoteColor = new QuoteColor(userQuote.getId(), config.getQuoteColor());
			recent(userQuote, quoteColor);
	
			persist.createQuoteColor(quoteColor);
			persist.createUserQuote(userQuote);		
			WidgetHelper.updateRecentQuotesWidgets(context);
		}
		catch (RestClientException e) {
			//TODO error handling
			Log.d(TAG, "getNewRandomQuote", e);
		}
	}

	@Background
	public void setFavorite(UserQuote userQuote, boolean favorited) {
		userQuote.setFavorited(favorited);
		favorite(userQuote, favorited);
		
		//TODO application.getClient().getRest().editUserQuote(userQuote, config.getLanguageId(), config.getUserId(), userQuote.getId() );

		persist.updateUserQuote(userQuote);
		
	}
	
	@UiThread
	public void favorite(UserQuote userQuote, boolean favorited) {		
		if(favorited){
			favoriteUserQuoteAdapter.push(userQuote);
		}
		else{
			favoriteUserQuoteAdapter.remove(userQuote);
		}
		auxUserQuoteAdapter.notifyDataSetChanged();
		recentUserQuoteAdapter.notifyDataSetChanged();
	}

	@UiThread
	public void recent(UserQuote userQuote, QuoteColor color){
		quoteColorsList.add(color);
		
		recentUserQuoteAdapter.push(userQuote);
		
		Author author = new Author();
		author.setFullName( ((Quote)userQuote.getRootQuote().getQuotes().toArray()[0] ).getAuthorFullname()  );
		author.setId( ((Quote)userQuote.getRootQuote().getQuotes().toArray()[0] ).getRootAuthorId()  );
		author(author);
		
		Category category = new Category();
		category.setDescription( ((Quote)userQuote.getRootQuote().getQuotes().toArray()[0] ).getCategoryDescription()  );
		category.setId( ((Quote)userQuote.getRootQuote().getQuotes().toArray()[0] ).getRootCategoryId()  );
		category(category);
		
		userQuoteList.put(userQuote.getId(), userQuote);
	}
	
	@UiThread
	public void author(Author author){
		authorAdapter.add(author);
	}

	@UiThread
	public void category(Category category){
		categoryAdapter.add(category);
	}
	
	public void changeAllColors(int colorRes) {
		//Atualiza a cor de todas as citações cadastradas no banco
		persist.updateAllQuoteColors(colorRes);

		//TODO atualizar somente as citações que estão carregadas
		for(QuoteColor quoteColor : quoteColorsList)
			quoteColor.setColorRes(colorRes);
		
		recentUserQuoteAdapter.notifyDataSetChanged();
	}

	public UserQuote getRandomUserQuote(){
		
		if( userQuoteList.size() == 0) return null;
		
		Random r = new Random();
		int i = r.nextInt(userQuoteList.size());
		
		return userQuoteList.valueAt(i);
	}
	
	public UserQuoteAdapter getAuthorUserQuoteAdapter(String authorId) {
		Log.d(TAG, "getAuthorUserQuoteAdapter - " + authorId);
		auxUserQuoteAdapter.clear();
		
		for(int ind =0; ind<userQuoteList.size(); ind++)
			if( ((Quote)userQuoteList.get(userQuoteList.keyAt(ind)).getRootQuote().getQuotes().toArray()[0]).getRootAuthorId()== Integer.parseInt(authorId) ){
				auxUserQuoteAdapter.add( userQuoteList.get(userQuoteList.keyAt(ind)) );
			}
		

		return auxUserQuoteAdapter;
	}

	public UserQuoteAdapter getCategoryUserQuoteAdapter(String categoryId) {
		
		auxUserQuoteAdapter.clear();
		
		for(int ind =0; ind<userQuoteList.size(); ind++)
			if( ((Quote)userQuoteList.get(userQuoteList.keyAt(ind)).getRootQuote().getQuotes().toArray()[0]).getRootCategoryId()== Integer.parseInt(categoryId) ){
				auxUserQuoteAdapter.add(userQuoteList.get(userQuoteList.keyAt(ind)));
			}
		
		auxUserQuoteAdapter.setColorQuotes(quoteColorsList);
		
		return auxUserQuoteAdapter;
	}

	public UserQuoteAdapter getSearchUserQuoteAdapter(String search) {
		
		auxUserQuoteAdapter.clear();
		
		for(int ind =0; ind<userQuoteList.size(); ind++)
			if( ((Quote)userQuoteList.get(userQuoteList.keyAt(ind)).getRootQuote().getQuotes().toArray()[0]).getText().toLowerCase().contains(search.toLowerCase()) ){
				auxUserQuoteAdapter.add(userQuoteList.get(userQuoteList.keyAt(ind)));
			}
		
		auxUserQuoteAdapter.setColorQuotes(quoteColorsList);
		
		return auxUserQuoteAdapter;
	}
}
