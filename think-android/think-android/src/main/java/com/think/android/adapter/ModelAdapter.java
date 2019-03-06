package com.think.android.adapter;

import java.util.Arrays;
import java.util.Collection;

import com.think.android.util.ModelSparseArray;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import br.com.think.model.ModelBase;

/**
 * 
 */
public abstract class ModelAdapter<T extends ModelBase> extends BaseAdapter {

	/**
     * Contains the list of objects that represent the data of this ArrayAdapter.
     * The content of this list is referred to as "the array" in the documentation.
     */
    private ModelSparseArray<T> mObjects;

    /**
     * Lock used to modify the content of {@link #mObjects}. Any write operation
     * performed on the array should be synchronized on this lock. This lock is also
     * used by the filter (see {@link #getFilter()} to make a synchronized copy of
     * the original array of data.
     */
    private final Object mLock = new Object();

    /**
     * Indicates whether or not {@link #notifyDataSetChanged()} must be called whenever
     * {@link #mObjects} is modified.
     */
    private boolean mNotifyOnChange = true; 
    
    private ModelSparseArray<T> mOriginalValues;
   
    /**
     * 
     * @param objects
     */
    public ModelAdapter(ModelSparseArray<T> objects) {
        init(objects);
    }
    
    /**
     * 
     * @param objects
     */
    public ModelAdapter() {
        init(new ModelSparseArray<T>());
    }

    /**
     * Adds the specified object at the end of the array.
     *
     * @param object The object to add at the end of the array.
     */
    public void addAll(T[] array) {
        if (mOriginalValues != null) {
            synchronized (mLock) {
                mOriginalValues.putAll(Arrays.asList(array));
                if (mNotifyOnChange) notifyDataSetChanged();
            }
        } else {
            mObjects.putAll(Arrays.asList(array));
            if (mNotifyOnChange) notifyDataSetChanged();
        }
    }

    /**
     * Adds the specified object at the end of the array.
     *
     * @param object The object to add at the end of the array.
     */
    public void addAll(Collection<? extends T> collection) {
        if (mOriginalValues != null) {
            synchronized (mLock) {
                mOriginalValues.putAll(collection);
                if (mNotifyOnChange) notifyDataSetChanged();
            }
        } else {
            mObjects.putAll(collection);
            if (mNotifyOnChange) notifyDataSetChanged();
        }
    }
    
    /**
     * Adds the specified object at the end of the array.
     *
     * @param object The object to add at the end of the array.
     */
    public void add(T object) {
        if (mOriginalValues != null) {
            synchronized (mLock) {
                mOriginalValues.add(object);
                if (mNotifyOnChange) notifyDataSetChanged();
            }
        } else {
            mObjects.add(object);
            if (mNotifyOnChange) notifyDataSetChanged();
        }
    }

    /**
     * Adds the specified object at the start of the array.
     *
     * @param object The object to add at the end of the array.
     */
    public void push(T object) {
        if (mOriginalValues != null) {
            synchronized (mLock) {
                mOriginalValues.push(object);
                if (mNotifyOnChange) notifyDataSetChanged();
            }
        } else {
            mObjects.push(object);
            if (mNotifyOnChange) notifyDataSetChanged();
        }
    }
    
    /**
     * Removes the specified object from the array.
     *
     * @param object The object to remove.
     */
    public void remove(T object) {
        if (mOriginalValues != null) {
            synchronized (mLock) {
                mOriginalValues.delete(object);
            }
        } else {
            mObjects.delete(object);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Remove all elements from the list.
     */
    public void clear() {
        if (mOriginalValues != null) {
            synchronized (mLock) {
                mOriginalValues.clear();
            }
        } else {
            mObjects.clear();
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }

    /**
     * Control whether methods that change the list ({@link #add},
     * {@link #insert}, {@link #remove}, {@link #clear}) automatically call
     * {@link #notifyDataSetChanged}.  If set to false, caller must
     * manually call notifyDataSetChanged() to have the changes
     * reflected in the attached view.
     *
     * The default is true, and calling notifyDataSetChanged()
     * resets the flag to true.
     *
     * @param notifyOnChange if true, modifications to the list will
     *                       automatically call {@link
     *                       #notifyDataSetChanged}
     */
    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }

    private void init(ModelSparseArray<T> objects) {
        mObjects = objects;
    }

    /**
     * {@inheritDoc}
     */
    public int getCount() {
        return mObjects.size();
    }

    /**
     * {@inheritDoc}
     */
    public T getItem(int position) {
        return mObjects.valueAt(position);
    }

    /**
     * {@inheritDoc}
     */
    public T getItemById(int id) {
        return mObjects.getByKey(id);
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     *
     * @return The position of the specified item.
     */
    public int getPosition(T item) {
        return mObjects.indexOf(item);
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     *
     * @return The position of the specified item.
     */
    public int getPosition(int id) {
        return mObjects.indexOfKey(id);
    }
    
    /**
     * {@inheritDoc}
     */
    public long getItemId(int position) {
        return mObjects.valueAt(position).getId();
    }

    /**
     * {@inheritDoc}
     */
    public abstract View getView(int position, View convertView, ViewGroup parent);

}
