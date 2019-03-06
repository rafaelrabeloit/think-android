
package com.think.android.dao;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.googlecode.androidannotations.annotations.EBean;
import com.think.android.model.QuoteColor;

@EBean
public class QuoteColorDAO extends GenericModelDAO<QuoteColor> {

	public static final String TABLE_NAME = "quote_color";
	public static final String FIELD_USER_QUOTE_ID = "user_quote_id";
	public static final String FIELD_COLOR_RES = "color_res";
	
	@Override
	public void createOrUpdate(SQLiteDatabase db, QuoteColor object) throws SQLException {
		ContentValues values = new ContentValues();
	    
	    values.put("id", object.getId() );
	    values.put(FIELD_USER_QUOTE_ID, object.getUserQuoteId() );
	    values.put(FIELD_COLOR_RES, object.getColorRes() );
	    db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	    
	}

	@Override
	public List<QuoteColor> queryForAll(SQLiteDatabase db) throws SQLException {
		
		LinkedList<QuoteColor> result = new LinkedList<QuoteColor>();
		
	    Cursor cursor = 
	      db.query(TABLE_NAME, new String[]{"id", FIELD_USER_QUOTE_ID, FIELD_COLOR_RES}, 
	               null, null, 
	               null, null, 
	               null );
	    
	    cursor.moveToFirst();
	    while(!cursor.isAfterLast()) {
	    	
	    	QuoteColor userQuote = new QuoteColor();
	    	
	    	userQuote.setId( cursor.getInt(0) );
	    	userQuote.setUserQuoteId( cursor.getInt(1) );
	    	userQuote.setColorRes( cursor.getInt(2) );

	    	result.add(userQuote);
	      
	    	cursor.moveToNext();
	    }
	    
	    cursor.close();
	    
		return result;
	}
	
	@Override
	public QuoteColor queryForId(SQLiteDatabase db, QuoteColor object)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<QuoteColor> queryForEq(SQLiteDatabase db, String field, String value, boolean num)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateAll(SQLiteDatabase db, String field, int value) {
		ContentValues values = new ContentValues();
	    
	    values.put(field, value );
	    db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		db.update(TABLE_NAME, values, null, null);
	}
}

