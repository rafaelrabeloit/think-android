package com.think.android.fragment;

import android.os.Bundle;
import br.com.think.model.UserQuote;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;
import com.think.android.activity.MainActivity;
import com.think.android.R;
import com.think.android.application.ThinkDataSource;

@EFragment(R.layout.fragment_list_wide_quotes)
public class CategoryQuotesFragment extends ItemListFragment<UserQuote>  {

	@Bean
	ThinkDataSource dataSource;

	@FragmentArg
	String categoryId;
	
	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public CategoryQuotesFragment() {}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.setActivateOnItemClick( ((MainActivity) this.getActivity() ).isTwoPane() );
	}
	
	@AfterInject
	public void bindAdapter(){

		this.setArrayAdapter( dataSource.getCategoryUserQuoteAdapter(categoryId) );
	}

}
