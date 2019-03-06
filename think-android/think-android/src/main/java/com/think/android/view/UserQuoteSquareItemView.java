package com.think.android.view;

import android.content.Context;
import android.graphics.Typeface;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.com.think.model.Quote;
import br.com.think.model.UserQuote;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.think.android.R;
import com.think.android.preference.UserConfiguration;

@EViewGroup(R.layout.item_view_square_quote)
public class UserQuoteSquareItemView extends LinearLayout {
	
	@Bean
	UserConfiguration config;
	
	@ViewById(R.id.text_preview_quote)
	TextView textQuoteText;
	
	@ViewById(R.id.text_author_fullName)
	TextView textAuthorFullName;

	@ViewById(R.id.layout_quote_item)
	RelativeLayout quoteContainer;
	
	boolean activateOnItemClick = false;
	
	public UserQuoteSquareItemView(Context context) {
	    super(context);
	}

	/**
	 * 
	 * @param userQuote
	 * @param position
	 * @param color
	 */
	public void bind(UserQuote userQuote, int color, boolean activateOnItemClick) {
		
		//Get the quote in certain language
		Quote quote = (Quote) userQuote.getRootQuote().getQuotes().toArray()[0];
    	
		//Typeface for Custom Font
    	Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/SEGOESC.TTF");
        
        if (textQuoteText != null) {
        	ViewTreeObserver observer = textQuoteText.getViewTreeObserver();        	
        	observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	        	@SuppressWarnings("deprecation")
				@Override
	        	public void onGlobalLayout() {
	        	    int maxLines = (int) textQuoteText.getHeight()
	        	            / textQuoteText.getLineHeight();
	        	    textQuoteText.setMaxLines(maxLines);
	        	    textQuoteText.getViewTreeObserver().removeGlobalOnLayoutListener(this);
	        	}
        	});
        	
        	textQuoteText.setText( quote.getText() );  
        	textQuoteText.setTextColor( this.getContext().getResources().getColor( config.getForegroundColor()) );
        	textQuoteText.setClickable(false);
        	textQuoteText.setFocusable(false);
        	textQuoteText.setFocusableInTouchMode(false);        	
        }      
        
        if (textAuthorFullName != null)  {
        	textAuthorFullName.setClickable(false);
        	textAuthorFullName.setFocusable(false);
        	textAuthorFullName.setFocusableInTouchMode(false);
        	
        	textAuthorFullName.setText( quote.getAuthorFullname() ); 
        	textAuthorFullName.setTextColor( this.getContext().getResources().getColor(config.getForegroundColor()) );
        	textAuthorFullName.setTypeface(tf,Typeface.NORMAL);
        }

        if (quoteContainer != null)  {
        	quoteContainer.setClickable(false);
        	quoteContainer.setFocusable(false);
        	quoteContainer.setFocusableInTouchMode(false);
        	
        	quoteContainer.setBackgroundResource( color );
        }
        
        this.setBackgroundResource(R.drawable.selectable_background_think);
        
        if(activateOnItemClick){
        	this.setFocusable(true);
        	//this.setFocusableInTouchMode(true);
        }
	}
}
