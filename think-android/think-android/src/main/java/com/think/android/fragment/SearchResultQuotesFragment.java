package com.think.android.fragment;

import android.app.Fragment;
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
import com.googlecode.androidannotations.annotations.FragmentArg;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.think.android.activity.MainActivity;
import com.think.android.adapter.UserQuoteAdapter;
import com.think.android.R;
import com.think.android.application.ThinkDataSource;
import com.think.android.view.UserQuoteSquareItemView;

@EFragment(R.layout.fragment_list_square_quotes)
public class SearchResultQuotesFragment extends Fragment  {

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
	
	@FragmentArg
	String search;

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	SearchResultQuotesFragment instance = this;

	boolean activateOnItemClick = false;
	
	OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			((MainActivity) instance.getActivity() ).onItemSelected( (String)v.getTag() );
		}
	};
	
	public SearchResultQuotesFragment() {}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.setActivateOnItemClick( ((MainActivity) this.getActivity() ).isTwoPane() );
	}
		
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		this.activateOnItemClick = activateOnItemClick;
	}
	
	@AfterViews
	public void bindAdapter(){
		UserQuoteAdapter searchs = dataSource.getSearchUserQuoteAdapter(search);
		
		layoutRoot.removeAllViews();

		if(searchs.getCount()> 0){
			scrollLayoutRoot.setVisibility(View.VISIBLE);
			
			int i = 0;
			while(i < searchs.getCount() ){
				
				LinearLayout dupla = new LinearLayout(this.getActivity());
				dupla.setOrientation(LinearLayout.HORIZONTAL);
				
				UserQuoteSquareItemView square = UserQuoteSquareItemView_.build(this.getActivity());
				square.bind(searchs.getItem(i), dataSource.getColorQuotes().getByKey(searchs.getItem(i).getId()).getColorRes(), activateOnItemClick );
				square.setTag( String.valueOf(  searchs.getItem(i).getId() ) );
				square.setOnClickListener(clickListener);
				dupla.addView( square );
				
				i++;
				if(i < searchs.getCount()){
					UserQuoteSquareItemView square2 = UserQuoteSquareItemView_.build(this.getActivity());
					square2.bind(searchs.getItem(i), dataSource.getColorQuotes().getByKey(searchs.getItem(i).getId()).getColorRes(), activateOnItemClick );
					square2.setTag( String.valueOf( searchs.getItem(i).getId() ) );
					square2.setOnClickListener(clickListener);
					dupla.addView( square2 );
				}
				
				layoutRoot.addView(dupla);
				i++;
			}
		}
		else {

			scrollLayoutRoot.setVisibility(View.GONE);
			
			empty.setVisibility(View.VISIBLE);
			empty.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			//empty.setText(errorMessage);
		}
	}

}
