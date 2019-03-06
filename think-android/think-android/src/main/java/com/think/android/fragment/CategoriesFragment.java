package com.think.android.fragment;

import br.com.think.model.Category;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.think.android.R;
import com.think.android.application.ThinkDataSource;

@EFragment(R.layout.fragment_list_categories)
public class CategoriesFragment extends ItemListFragment<Category>  {

	@Bean
	ThinkDataSource dataSource;
	
	public CategoriesFragment() {}

	@AfterInject
	public void bindAdapter(){

		this.setArrayAdapter( dataSource.getCategoryAdapter() );
	}

}
