package com.think.android.util;

import java.util.Collection;
import java.util.LinkedList;

import android.util.SparseIntArray;
import br.com.think.model.ModelBase;

public class ModelSparseArray<T extends ModelBase> extends LinkedList<T> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	SparseIntArray set = new SparseIntArray();
	
	@Override
	public void push(T e) {
		int p = set.get(e.getId(), -1);
		if( p == -1 ){
			set.put(e.getId(), super.size());
			super.push(e);
		}
		else{
			super.set(p, e);
		}
	}
	
	@Override
	public boolean add(T e) {

		int p = set.get(e.getId(), -1);
		if( p == -1 ){
			set.put(e.getId(), super.size());
			return super.add(e);
		}
		else{
			super.set(p, e);
			return true;
		}
	};
	
	public void append(T e){
		add(e);
	}
	
	public void delete(T e){

		int p = set.get(e.getId(), -1);
		if( p != -1 ){	
			set.removeAt( p );
			super.remove(p);
		}
	}
	
	public boolean putAll(Collection<? extends T> collection){
		for(T v : collection)
			if ( !add(v) ) return false;
		return true;
	}
	
	@Override
	public boolean addAll(java.util.Collection<? extends T> collection) {
		return putAll(collection);
	};

	public T valueAt(int position) {
		return super.get(position);
	}

	public T getByKey(int id) {

		int p = set.get( id, -1);
		if( p != -1 ){	
			return super.get(p);
		}
		return null;
	}
	
	public int indexOfKey(int id) {
		return set.get( id, -1);		
	}
	
	@Override
	public void clear() {
		super.clear();
		set.clear();
	}
}
