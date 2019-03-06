package com.think.android.fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.TextView;
import br.com.think.model.Quote;
import br.com.think.model.UserQuote;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;
import com.googlecode.androidannotations.annotations.ViewById;
import com.think.android.activity.MainActivity;
import com.think.android.activity.QuoteDetailActivity;
import com.think.android.R;
import com.think.android.application.ThinkDataSource;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link ItemListActivity} in two-pane mode (on tablets) or a
 * {@link QuoteDetailActivity} on handsets.
 */
@EFragment(R.layout.fragment_quote_detail)
public class QuoteDetailFragment extends Fragment {
	
	/**
	 * 
	 */
	private UserQuote userQuote;
	
	@ViewById(R.id.text_quote_text)
	TextView textQuoteText;

	@ViewById(R.id.text_quote_author)
	TextView textAuthorFullName;

	@ViewById(R.id.text_quote_category)
	TextView textCategoryDescription;

	@ViewById(R.id.text_quote_reference)
	TextView textReferenceDescription;

	@FragmentArg
	String quoteId;
	
	@Bean
	ThinkDataSource dataSource;

	public UserQuote getUserQuote() {
		return userQuote;
	}

	public void setUserQuote(UserQuote userQuote) {
		this.userQuote = userQuote;
	}
	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public QuoteDetailFragment() { }

	@AfterInject
	public void loadQuote(){

		Integer id;			
		try{
			id = Integer.parseInt ( quoteId );
			userQuote = dataSource.getUserQuoteList().get( id );
			
		}
		catch(NumberFormatException ex){
			Log.d(MainActivity.TAG, "Error selecting quote.");
		}
		
	}

	@AfterViews
	public void bindView() {

		if ((userQuote != null && userQuote.getRootQuote() != null &&  userQuote.getRootQuote().getQuotes() != null) ) {
			Quote quote = (Quote) userQuote.getRootQuote().getQuotes().toArray()[0];

			//Typeface for Custom Font
	    	Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/SEGOESC.TTF");
			
			textQuoteText.setText( quote.getText() );

			textAuthorFullName.setText( quote.getAuthorFullname() );
			textAuthorFullName.setTypeface(tf,Typeface.NORMAL);

			textCategoryDescription.setText( quote.getCategoryDescription() );
			textCategoryDescription.setTypeface(tf,Typeface.NORMAL);

			textReferenceDescription.setText( quote.getReferenceDescription() );
			textReferenceDescription.setTypeface(tf,Typeface.NORMAL);
			
		}		
	}
}
