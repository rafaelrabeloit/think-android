package com.think.android.data;

import java.util.HashSet;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import br.com.think.model.Language;
import br.com.think.model.Quote;
import br.com.think.model.RootQuote;
import br.com.think.model.UserQuote;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.Transactional;
import com.think.android.dao.LanguageDAO;
import com.think.android.dao.QuoteColorDAO;
import com.think.android.dao.QuoteDAO;
import com.think.android.dao.RootQuoteDAO;
import com.think.android.dao.UserQuoteDAO;
import com.think.android.model.QuoteColor;

@EBean
public class DataPersistence {

	static private final String TAG = "DataPersistence";

	@Bean
	DatabaseHelper helper;

	// the DAO object we use to access the SimpleData table
	private UserQuoteDAO userQuoteDAO = null;

	// the DAO object we use to access the SimpleData table
	private RootQuoteDAO rootQuoteDAO = null;

	// the DAO object we use to access the SimpleData table
	private QuoteDAO quoteDAO = null;
	
	// the DAO object we use to access the SimpleData table
	private QuoteColorDAO quoteColorDAO = null;

	// the DAO object we use to access the SimpleData table
	private LanguageDAO languageDAO = null;
	
	@AfterInject
	void init(){

		userQuoteDAO = helper.getUserQuoteDAO();
		rootQuoteDAO = helper.getRootQuoteDAO();
		quoteDAO = helper.getQuoteDAO();
		quoteColorDAO = helper.getQuoteColorDAO();
		languageDAO = helper.getLanguageDAO();
		
	}
	
	public void createLanguage(Language language){
		Log.i(TAG, "createLanguage");
		
		SQLiteDatabase db = helper.getWritableDatabase();
		
		languageDAO.createOrUpdate(db, language);		
		
		Log.i(TAG, "created new entries in persistLanguage");
	}
	
	
	public List<Language> queryForAllLanguages(){
		Log.i(TAG, "queryForAllLanguages");

		SQLiteDatabase db = helper.getReadableDatabase();
		
		List<Language> list = languageDAO.queryForAll(db);
		
		
		return list;
	}

	
	
	public void createUserQuote(UserQuote userQuote){
		Log.i(TAG, "createUserQuote");
		
		SQLiteDatabase db = helper.getWritableDatabase();
		
		userQuoteDAO.createOrUpdate(db, userQuote);
		
		rootQuoteDAO.createOrUpdate(db, userQuote.getRootQuote());
		
		for(Quote q : userQuote.getRootQuote().getQuotes() ){
			q.setRootQuote(userQuote.getRootQuote());
			quoteDAO.createOrUpdate(db, q);
		}
		
		Log.i(TAG, "created new entries in persistUserQuote");
	}
	
	@Transactional
	void create(SQLiteDatabase db, UserQuote userQuote){
		
	}
	
	public UserQuote queryLastUserQuote(){
		SQLiteDatabase db = helper.getReadableDatabase();
		
		UserQuote userQuote = userQuoteDAO.queryForLast(db);
		
		if(userQuote==null) return null;
		
		RootQuote rootQuote = rootQuoteDAO.queryForEq(db, "id", userQuote.getRootQuote().getId().toString(), true ).get(0);
		
		rootQuote.setQuotes( new HashSet<Quote>(quoteDAO.queryForEq(db, "root_quote_id", rootQuote.getId().toString(), true)) );
		
		userQuote.setRootQuote(rootQuote);
		
		return userQuote;
	}
	
	
	public List<UserQuote> queryForAllUserQuote(){
		Log.i(TAG, "queryForAllUserQuote");

		SQLiteDatabase db = helper.getReadableDatabase();
		
		List<UserQuote> list = userQuoteDAO.queryForAll(db);
		
		for(UserQuote userQuote : list){
			List<RootQuote> roots = rootQuoteDAO.queryForEq(db, "id", userQuote.getRootQuote().getId().toString(), true );
			
			if(roots.size()>0){
				RootQuote rootQuote = rootQuoteDAO.queryForEq(db, "id", userQuote.getRootQuote().getId().toString(), true ).get(0);
				
				rootQuote.setQuotes( new HashSet<Quote>(quoteDAO.queryForEq(db, "root_quote_id", rootQuote.getId().toString(), true)) );
				
				userQuote.setRootQuote(rootQuote);
			}
			else{
				Log.wtf(TAG, "UserQuote without RootQuote");
				list.remove(userQuote);
			}
		}
		
		return list;
	}

	public void updateUserQuote(UserQuote userQuote) {
		Log.i(TAG, "updateUserQuote");
	
		SQLiteDatabase db = helper.getWritableDatabase();
		
		userQuoteDAO.createOrUpdate(db, userQuote);
	}

	public List<QuoteColor> queryForAllQuoteColors() {

		SQLiteDatabase db = helper.getReadableDatabase();
		
		List<QuoteColor> list = quoteColorDAO.queryForAll(db);
		
		return list;
	}

	public void createQuoteColor(QuoteColor quoteColor) {
		Log.i(TAG, "createQuoteColor");
		
		SQLiteDatabase db = helper.getWritableDatabase();
		
		quoteColorDAO.createOrUpdate(db, quoteColor);
		
	}
	
	public void updateAllQuoteColors(int colorRes) {
		Log.i(TAG, "updateUserQuote");
	
		SQLiteDatabase db = helper.getWritableDatabase();
		
		quoteColorDAO.updateAll(db, QuoteColorDAO.FIELD_COLOR_RES, colorRes);
	}
}
