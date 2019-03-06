package com.think.android.fragment;

import br.com.think.model.Author;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.think.android.R;
import com.think.android.application.ThinkDataSource;

@EFragment(R.layout.fragment_list_authors)
public class AuthorsFragment extends ItemListFragment<Author>  {

	@Bean
	ThinkDataSource dataSource;
	
	public AuthorsFragment() {}

	@AfterInject
	public void bindAdapter(){
		this.setArrayAdapter( dataSource.getAuthorAdapter() );
	}

}
