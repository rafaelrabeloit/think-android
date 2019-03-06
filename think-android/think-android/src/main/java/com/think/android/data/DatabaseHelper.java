package com.think.android.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import br.com.think.model.Language;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;
import com.think.android.dao.LanguageDAO;
import com.think.android.dao.QuoteColorDAO;
import com.think.android.dao.QuoteDAO;
import com.think.android.dao.RootQuoteDAO;
import com.think.android.dao.UserQuoteDAO;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
@EBean(scope = Scope.Singleton)
@SuppressWarnings("unused")
public class DatabaseHelper extends SQLiteOpenHelper {

	static private final String TAG = "DatabaseHelper";
	
	// name of the database file for your application 
	private static final String DATABASE_NAME = "think_database.sqlite";
	
	// any time you make changes to your database objects, you may have to increase the database version
	private static final int DATABASE_VERSION = 1;

	private static final String USERQUOTE_TABLE_DROP = "DROP TABLE "+UserQuoteDAO.TABLE_NAME+"; ";
	private static final String USERQUOTE_TABLE_CREATE = "CREATE TABLE "+UserQuoteDAO.TABLE_NAME+" ( id INT, " +
			UserQuoteDAO.FIELD_ROOT_QUOTE_ID + " INT, " + 
			UserQuoteDAO.FIELD_FAVORITED + " INT(1), " + 
			UserQuoteDAO.FIELD_RECEIVED_ON + " INT, " + 
			" primary key(id) ) ; ";

	private static final String LANGUAGE_TABLE_DROP = "DROP TABLE " + LanguageDAO.TABLE_NAME + "; ";
	private static final String LANGUAGE_TABLE_CREATE = "CREATE TABLE " + LanguageDAO.TABLE_NAME + " ( id INT, " + 
			LanguageDAO.FIELD_LANG + " TEXT, " + 
			" primary key(id) ) ; ";
	
	private static final String ROOTQUOTE_TABLE_DROP = "DROP TABLE " + RootQuoteDAO.TABLE_NAME + "; ";
	private static final String ROOTQUOTE_TABLE_CREATE = "CREATE TABLE " + RootQuoteDAO.TABLE_NAME + " ( id INT, " + 
			RootQuoteDAO.FIELD_STATUS + " TEXT, " + 
			" primary key(id) ) ; ";

	private static final String QUOTE_TABLE_DROP = "DROP TABLE " + QuoteDAO.TABLE_NAME + "; ";
	private static final String QUOTE_TABLE_CREATE = "CREATE TABLE " + QuoteDAO.TABLE_NAME + " ( id INT, " + 
			QuoteDAO.FIELD_ROOT_QUOTE_ID+" INT, " + 
			QuoteDAO.FIELD_TEXT+" TEXT, " + 
			QuoteDAO.FIELD_AUTHOR_FULLNAME+" TEXT, " + 
			QuoteDAO.FIELD_CATEGORY_DESCRIPTION+" TEXT, " + 
			QuoteDAO.FIELD_REFERENCE_DESCRIPTION+" TEXT, " + 
			QuoteDAO.FIELD_ROOT_AUTHOR_ID+" INT, " + 
			QuoteDAO.FIELD_ROOT_REFERENCE_ID+" INT, " + 
			QuoteDAO.FIELD_ROOT_CATEGORY_ID+" INT, " + 
			" primary key(id) ) ; ";

	private static final String QUOTECOLOR_TABLE_DROP = "DROP TABLE " + QuoteColorDAO.TABLE_NAME + "; ";
	private static final String QUOTECOLOR_TABLE_CREATE = "CREATE TABLE " + QuoteColorDAO.TABLE_NAME + " ( id INT, " + 
			QuoteColorDAO.FIELD_USER_QUOTE_ID + " INT, " + 
			QuoteColorDAO.FIELD_COLOR_RES + " INT, " + 
			" primary key(id) ) ; ";
	
	@Bean
	UserQuoteDAO userQuoteDAO;

	@Bean
	RootQuoteDAO rootQuoteDAO;

	@Bean
	QuoteDAO quoteDAO;

	@Bean
	QuoteColorDAO quoteColorDAO;

	@Bean
	LanguageDAO languageDAO;
	
	/**
	 * Constructor
	 * @param context
	 */
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * This is called when the database is first created. Usually you should call createTable statements here to create
	 * the tables that will store your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG, "onCreate");
		
		db.execSQL(USERQUOTE_TABLE_CREATE);
		db.execSQL(ROOTQUOTE_TABLE_CREATE);
		db.execSQL(QUOTE_TABLE_CREATE);
		db.execSQL(QUOTECOLOR_TABLE_CREATE);
		db.execSQL(LANGUAGE_TABLE_CREATE);

	}

	/**
	 * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
	 * the various data to match the new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "onUpgrade");
		
		/*
		db.execSQL(USERQUOTE_TABLE_DROP);
		db.execSQL(ROOTQUOTE_TABLE_DROP);
		db.execSQL(QUOTE_TABLE_DROP);
		db.execSQL(QUOTECOLOR_TABLE_DROP);
		*/
		
		if(oldVersion==1){
			switch (newVersion) {
			case 2:

				db.execSQL(LANGUAGE_TABLE_CREATE);

				//Default fallback
				Language lang = new Language("en_US", "US English");
				lang.setId(1);
				languageDAO.createOrUpdate(db, lang);
				
				break;

			default:
				break;
			}
		}
		
		// after we drop the old databases, we create the new ones
		//onCreate(db);
	}

	/**
	 * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
	 * value.
	 */
	public UserQuoteDAO getUserQuoteDAO() {
		return userQuoteDAO;
	}
	
	/**
	 * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
	 * value.
	 */
	public RootQuoteDAO getRootQuoteDAO() {
		return rootQuoteDAO;
	}
	
	/**
	 * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
	 * value.
	 */
	public QuoteDAO getQuoteDAO() {
		return quoteDAO;
	}

	/**
	 * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
	 * value.
	 */
	public QuoteColorDAO getQuoteColorDAO() {
		return quoteColorDAO;
	}

	/**
	 * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
	 * value.
	 */
	public LanguageDAO getLanguageDAO() {
		return languageDAO;
	}
	
	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
	}
}
