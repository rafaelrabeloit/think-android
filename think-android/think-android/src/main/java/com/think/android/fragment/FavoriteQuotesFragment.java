package com.think.android.fragment;

import android.app.Fragment;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.think.android.view.UserQuoteSquareItemView_;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.think.android.activity.MainActivity;
import com.think.android.adapter.UserQuoteAdapter;
import com.think.android.R;
import com.think.android.application.ThinkDataSource;
import com.think.android.view.UserQuoteSquareItemView;

@EFragment(R.layout.fragment_list_square_quotes)
public class FavoriteQuotesFragment extends Fragment {

	@Bean
	ThinkDataSource dataSource;
	
	@ViewById(R.id.layout_root)
	LinearLayout layoutRoot;
	
	@ViewById(R.id.scroll_layout_root)
	ScrollView scrollLayoutRoot;

	@ViewById(android.R.id.empty)
	TextView empty;
	
	@StringRes(R.string.text_error_no_fav_quotes)
	String errorMessage;	
	
	FavoriteQuotesFragment instance = this;

	boolean activateOnItemClick = false;
	
	private UserQuoteAdapter userQuotesAdapter;
			
	/**
	 * Listener to call onItemSelected on the MainActivity
	 */
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			((MainActivity) instance.getActivity() ).onItemSelected( (String)v.getTag() );
		}
	};

	/**
	 * Identifies changes on the data set
	 */
	private DataSetObserver observer = new DataSetObserver() {
		
		@Override
		public void onChanged() {
			bindAdapter();
		}
	};
	
	public FavoriteQuotesFragment(){ }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.setActivateOnItemClick( ((MainActivity) this.getActivity() ).isTwoPane() );
	}
	
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		this.activateOnItemClick = activateOnItemClick;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		userQuotesAdapter.unregisterDataSetObserver(observer);
	}
	
	@AfterViews
	public void bindAdapter(){
		userQuotesAdapter = dataSource.getFavoriteUserQuoteAdapter();
		userQuotesAdapter.registerDataSetObserver(observer);

		layoutRoot.removeAllViews();
		
		if(userQuotesAdapter.getCount()> 0){
			scrollLayoutRoot.setVisibility(View.VISIBLE);
			
			int i = 0;
			while(i < userQuotesAdapter.getCount() ){
				
				LinearLayout dupla = new LinearLayout(this.getActivity());
				dupla.setOrientation(LinearLayout.HORIZONTAL);
				
				UserQuoteSquareItemView square = UserQuoteSquareItemView_.build(this.getActivity());
				square.bind(userQuotesAdapter.getItem(i), dataSource.getColorQuotes().getByKey(userQuotesAdapter.getItem(i).getId()).getColorRes(), activateOnItemClick );
				square.setTag( String.valueOf(  userQuotesAdapter.getItem(i).getId() ) );
				square.setOnClickListener(clickListener);
				dupla.addView( square );
				
				i++;
				if(i < userQuotesAdapter.getCount()){
					UserQuoteSquareItemView square2 = UserQuoteSquareItemView_.build(this.getActivity());
					square2.bind(userQuotesAdapter.getItem(i), dataSource.getColorQuotes().getByKey(userQuotesAdapter.getItem(i).getId()).getColorRes(), activateOnItemClick );
					square2.setTag( String.valueOf( userQuotesAdapter.getItem(i).getId() ) );
					square2.setOnClickListener(clickListener);
					dupla.addView( square2 );
				}
				
				layoutRoot.addView(dupla);	
				i++;
			}
		}
		else{
			scrollLayoutRoot.setVisibility(View.GONE);
			
			empty.setVisibility(View.VISIBLE);
			empty.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.think_no_favorite_quotes, 0, 0);
			empty.setText(errorMessage);
		}
	}
}
