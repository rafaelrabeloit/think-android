package com.think.android.dao;

import java.util.LinkedList;
import java.util.List;

import com.googlecode.androidannotations.annotations.EBean;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import br.com.think.model.Quote;
import br.com.think.model.RootQuote;

@EBean
public class QuoteDAO extends GenericModelDAO<Quote> {

	public static final String TABLE_NAME = "quote";
	public static final String FIELD_TEXT = "text";
	public static final String FIELD_ROOT_QUOTE_ID = "root_quote_id";
	public static final String FIELD_AUTHOR_FULLNAME = "author_fullname";
	public static final String FIELD_CATEGORY_DESCRIPTION = "category_description";
	public static final String FIELD_REFERENCE_DESCRIPTION = "reference_description";
	public static final String FIELD_ROOT_CATEGORY_ID = "root_category_id";
	public static final String FIELD_ROOT_REFERENCE_ID = "root_reference_id";
	public static final String FIELD_ROOT_AUTHOR_ID = "root_author_id";
	
	@Override
	public void createOrUpdate(SQLiteDatabase db, Quote object) throws SQLException {
		ContentValues values = new ContentValues();
	    
	    values.put("id", object.getId() );
	    values.put(FIELD_TEXT, object.getText() );
	    values.put(FIELD_ROOT_QUOTE_ID, object.getRootQuote().getId() );
	    values.put(FIELD_AUTHOR_FULLNAME, object.getAuthorFullname() );
	    values.put(FIELD_CATEGORY_DESCRIPTION, object.getCategoryDescription() );
	    values.put(FIELD_REFERENCE_DESCRIPTION, object.getReferenceDescription());
	    values.put(FIELD_ROOT_CATEGORY_ID, object.getRootCategoryId() );
	    values.put(FIELD_ROOT_REFERENCE_ID, object.getRootReferenceId() );
	    values.put(FIELD_ROOT_AUTHOR_ID, object.getRootAuthorId() );
	    db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		
	}

	@Override
	public List<Quote> queryForAll(SQLiteDatabase db) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Quote queryForId(SQLiteDatabase db, Quote object) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Quote> queryForEq(SQLiteDatabase db, String field, String value, boolean num)
			throws SQLException {

		LinkedList<Quote> result = new LinkedList<Quote>();
		
	    Cursor cursor = 
	    	db.query(TABLE_NAME, 
	    		new String[]{"id", FIELD_TEXT, FIELD_ROOT_QUOTE_ID, FIELD_AUTHOR_FULLNAME, FIELD_CATEGORY_DESCRIPTION, FIELD_REFERENCE_DESCRIPTION, FIELD_ROOT_AUTHOR_ID, FIELD_ROOT_CATEGORY_ID, FIELD_ROOT_REFERENCE_ID},
	    		field + " = " + (num ? "?" : "'?'") , new String[]{ value},
	    		null, null, 
	    		null );
	    
	    cursor.moveToFirst();
	    while(!cursor.isAfterLast()) {
	    	
	    	Quote quote = new Quote();
	    	
	    	quote.setId( cursor.getInt(0) );
	    	quote.setText( cursor.getString(1) );
	    	
	    	RootQuote rootQuote = new RootQuote();
	    	rootQuote.setId(cursor.getInt(2));
	    	quote.setRootQuote(rootQuote);
	    	
	    	quote.setAuthorFullname( cursor.getString(3) );
	    	quote.setCategoryDescription( cursor.getString(4) );
	    	quote.setReferenceDescription( cursor.getString(5) );

	    	quote.setRootAuthorId( cursor.getInt(6) );
	    	quote.setRootCategoryId( cursor.getInt(7) );
	    	quote.setRootReferenceId( cursor.getInt(8) );
	    	
	    	result.add(quote);
	      
	    	cursor.moveToNext();
	    }
	    
	    cursor.close();
	    
		return result;
	}


}
