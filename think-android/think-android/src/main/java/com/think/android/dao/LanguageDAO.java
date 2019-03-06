package com.think.android.dao;

import java.util.LinkedList;
import java.util.List;

import com.googlecode.androidannotations.annotations.EBean;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import br.com.think.model.Language;

@EBean
public class LanguageDAO extends GenericModelDAO<Language> {

	public static final String TABLE_NAME = "language";
	public static final String FIELD_LANG = "lang";
	
	@Override
	public void createOrUpdate(SQLiteDatabase db, Language object) throws SQLException {
		ContentValues values = new ContentValues();
	    
	    values.put("id", object.getId() );
	    values.put(FIELD_LANG, object.getCode() );
	    db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	    
	}

	@Override
	public List<Language> queryForAll(SQLiteDatabase db) throws SQLException {
		
		LinkedList<Language> result = new LinkedList<Language>();
		
	    Cursor cursor = 
	      db.query(false, TABLE_NAME, new String[]{"id", FIELD_LANG}, 
	               null, null, 
	               null, null, null, null);
	    
	    cursor.moveToFirst();
	    while(!cursor.isAfterLast()) {
	    	
	    	Language language = new Language();
	    	
	    	language.setId( cursor.getInt(0) );
	    	language.setCode( cursor.getString(1) );
	    		    	
	    	result.add(language);
	      
	    	cursor.moveToNext();
	    }
	    
	    cursor.close();
	    
		return result;
	}

	@Override
	public Language queryForId(SQLiteDatabase db, Language object)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Language> queryForEq(SQLiteDatabase db, String field, String value, boolean num)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}



}
