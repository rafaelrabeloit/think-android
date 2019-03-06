package com.think.android.fragment;

import android.os.Bundle;
import br.com.think.model.UserQuote;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.think.android.activity.MainActivity;
import com.think.android.R;
import com.think.android.application.ThinkDataSource;

@EFragment(R.layout.fragment_list_wide_quotes)
public class RecentQuotesFragment extends ItemListFragment<UserQuote> {

	@Bean
	ThinkDataSource dataSource;
	
	public RecentQuotesFragment(){ }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.setActivateOnItemClick( ((MainActivity) this.getActivity() ).isTwoPane() );
	}
			
	@AfterInject
	public void bindAdapter(){
		this.setArrayAdapter( dataSource.getRecentUserQuoteAdapter() );
	}
}
