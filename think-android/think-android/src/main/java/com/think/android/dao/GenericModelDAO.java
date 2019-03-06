package com.think.android.dao;

import java.util.List;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import br.com.think.model.ModelBase;

public abstract class GenericModelDAO<T extends ModelBase> {
	
	abstract void createOrUpdate(SQLiteDatabase db, T object) throws SQLException;
	
	abstract List<T> queryForAll(SQLiteDatabase db) throws SQLException;
	abstract T queryForId(SQLiteDatabase db, T object) throws SQLException;
	abstract List<T> queryForEq(SQLiteDatabase db, String field, String value, boolean num) throws SQLException;
	
}
