package com.think.android.fragment;

import com.think.android.adapter.ModelAdapter;

import br.com.think.model.ModelBase;
import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link QuoteDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public abstract class ItemListFragment< T extends ModelBase > extends ListFragment {

	private static String TAG = "ItemListFragment";
	
	private int list_layout;
	
	public int getListLayout(){
		return list_layout;
	}

	public void setListLayout(int i){
		list_layout = i;
	}	

	private ModelAdapter<T> arrayAdapter;
	
	public ModelAdapter<T> getArrayAdapter(){
		return arrayAdapter;
	}
	
	ItemListFragment<T> instance = this;
	
	public void setArrayAdapter(ModelAdapter<T> i){
		Log.d(TAG, "setArrayAdapter");
		
		arrayAdapter = i;
		
		// Set the ListAdapter		
		setListAdapter(arrayAdapter);
		
		Log.d(TAG, "arrayAdapter - " + arrayAdapter.getCount());
		
	}
	
	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = callbacks;

	public Callbacks getCallbacks() {
		return mCallbacks;
	}

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	private boolean activateOnItemClick;

	public boolean isActivateOnItemClick() {
		return activateOnItemClick;
	}

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(String id);
	}

	/**
	 * A implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks callbacks = new Callbacks() {
		
		@Override
		public void onItemSelected(String id) {}

	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ItemListFragment() { }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
				
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		return null;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.d(TAG, "onViewCreated");
		super.onViewCreated(view, savedInstanceState);
		
		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
		
		getListView().setDividerHeight(0);
	}

	@Override
	public void onAttach(Activity activity) {
		Log.d(TAG, "onAttach");
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		Log.d(TAG, "onDetach");
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = callbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		Log.d(TAG, "onListItemClick");
		super.onListItemClick(listView, view, position, id);

		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		mCallbacks.onItemSelected( getArrayAdapter().getItem(position).getId().toString() );
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.d(TAG, "onSaveInstanceState");
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		this.activateOnItemClick = activateOnItemClick;
		getListView().setChoiceMode(
			activateOnItemClick 
				? ListView.CHOICE_MODE_SINGLE
				: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
	
	
}
