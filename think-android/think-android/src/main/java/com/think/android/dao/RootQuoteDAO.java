package com.think.android.dao;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import br.com.think.model.RootQuote;

import com.googlecode.androidannotations.annotations.EBean;

@EBean
public class RootQuoteDAO extends GenericModelDAO<RootQuote> {

	public static final String TABLE_NAME = "root_quote";
	public static final String FIELD_STATUS = "status";
	
	@Override
	public void createOrUpdate(SQLiteDatabase db, RootQuote object)
			throws SQLException {
		ContentValues values = new ContentValues();
		
	    values.put("id", object.getId() );
	    values.put(FIELD_STATUS, object.getStatus() );
	    db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		
	}

	@Override
	public List<RootQuote> queryForAll(SQLiteDatabase db) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RootQuote queryForId(SQLiteDatabase db, RootQuote object)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RootQuote> queryForEq(SQLiteDatabase db, String field, String value, boolean num)
			throws SQLException {

		LinkedList<RootQuote> result = new LinkedList<RootQuote>();
				
	    Cursor cursor = 
	    	db.query(TABLE_NAME, new String[]{"id", FIELD_STATUS},
	    		field + " = " + (num ? "?" : "'?'") , new String[]{ value},
	    		null, null, 
	    		null );
	    
	    cursor.moveToFirst();
	    while(!cursor.isAfterLast()) {
	    	
	    	RootQuote userQuote = new RootQuote();
	    	
	    	userQuote.setId( cursor.getInt(0) );
	    	userQuote.setStatus( cursor.getString(1) );
	    	
	    	result.add(userQuote);
	      
	    	cursor.moveToNext();
	    }
	    
	    cursor.close();
	    
		return result;
	}

}
