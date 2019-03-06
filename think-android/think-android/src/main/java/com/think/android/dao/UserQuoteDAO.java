package com.think.android.dao;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.googlecode.androidannotations.annotations.EBean;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import br.com.think.model.RootQuote;
import br.com.think.model.UserQuote;

@EBean
public class UserQuoteDAO extends GenericModelDAO<UserQuote> {

	public static final String TABLE_NAME = "user_quote";
	public static final String FIELD_ROOT_QUOTE_ID = "root_quote_id";
	public static final String FIELD_FAVORITED = "favorited";
	public static final String FIELD_RECEIVED_ON = "received_on";
	
	@Override
	public void createOrUpdate(SQLiteDatabase db, UserQuote object) throws SQLException {
		ContentValues values = new ContentValues();
	    
	    values.put("id", object.getId() );
	    values.put(FIELD_ROOT_QUOTE_ID, object.getRootQuote().getId() );
	    values.put(FIELD_FAVORITED, object.isFavorited() );
	    values.put(FIELD_RECEIVED_ON, object.getReceivedOn().getTime() );
	    db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	    
	}

	@Override
	public List<UserQuote> queryForAll(SQLiteDatabase db) throws SQLException {
		
		LinkedList<UserQuote> result = new LinkedList<UserQuote>();
		
	    Cursor cursor = 
	      db.query(TABLE_NAME, new String[]{"id", FIELD_FAVORITED, FIELD_RECEIVED_ON, FIELD_ROOT_QUOTE_ID}, 
	               null, null, 
	               null, null, 
	               FIELD_RECEIVED_ON + " ASC" );
	    
	    cursor.moveToFirst();
	    while(!cursor.isAfterLast()) {
	    	
	    	UserQuote userQuote = new UserQuote();
	    	
	    	userQuote.setId( cursor.getInt(0) );
	    	userQuote.setFavorited( cursor.getInt(1)>0 );
	    	userQuote.setReceivedOn( new Date(cursor.getInt(2)) );
	    	
	    	RootQuote rootQuote = new RootQuote();
	    	rootQuote.setId(cursor.getInt(3));
	    	
	    	userQuote.setRootQuote(rootQuote);
	    	
	    	result.add(userQuote);
	      
	    	cursor.moveToNext();
	    }
	    
	    cursor.close();
	    
		return result;
	}

	public UserQuote queryForLast(SQLiteDatabase db) throws SQLException {
		
		UserQuote userQuote = null;
		
	    Cursor cursor = 
	      db.query(TABLE_NAME, new String[]{"id", FIELD_FAVORITED, FIELD_RECEIVED_ON, FIELD_ROOT_QUOTE_ID}, 
	               null, null, 
	               null, null, 
	               FIELD_RECEIVED_ON + " DESC", "1");
	    
	    cursor.moveToFirst();
	    while(!cursor.isAfterLast()) {
	    	userQuote = new UserQuote();
	    	
	    	userQuote.setId( cursor.getInt(0) );
	    	userQuote.setFavorited( cursor.getInt(1)>0 );
	    	userQuote.setReceivedOn( new Date(cursor.getInt(2)) );
	    	
	    	RootQuote rootQuote = new RootQuote();
	    	rootQuote.setId(cursor.getInt(3));
	    	
	    	userQuote.setRootQuote(rootQuote);
	    		      
	    	cursor.moveToNext();
	    }
	    
	    cursor.close();
	    
		return userQuote;
	}
	
	@Override
	public UserQuote queryForId(SQLiteDatabase db, UserQuote object)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserQuote> queryForEq(SQLiteDatabase db, String field, String value, boolean num)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}



}
