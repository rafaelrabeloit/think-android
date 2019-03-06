package com.think.android.view;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.think.model.Quote;
import br.com.think.model.UserQuote;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.think.android.R;
import com.think.android.preference.UserConfiguration;

@EViewGroup(R.layout.item_view_wide_quote)
public class UserQuoteWideItemView extends LinearLayout {
	
	OnClickListener favoriteClickListener = null;

	OnClickListener shareClickListener = null;
	
	@Bean
	UserConfiguration config;
	
	@ViewById(R.id.text_quote_preview)
	TextView textQuoteText;
	
	@ViewById(R.id.text_author_fullName)
	TextView textAuthorFullName;

	@ViewById(R.id.text_category_description)
	TextView textCategoryDescription;
	
	@ViewById(R.id.text_reference_description)
	TextView textReferenceDescription;

	@ViewById(R.id.button_favorite)
	ImageButton buttonFavorite;
	
	@ViewById(R.id.button_share)
	ImageButton buttonShare;

	@ViewById(R.id.image_quote_left_mark)
	ImageView quoteImage;

	@ViewById(R.id.layout_quote_item)
	LinearLayout quoteContainer;

	@ViewById(R.id.layout_options_container)
	LinearLayout optionsContainer;

	public OnClickListener getFavoriteClickListener() {
		return favoriteClickListener;
	}

	public void setFavoriteClickListener(OnClickListener favoriteClickListener) {
		this.favoriteClickListener = favoriteClickListener;
	}

	public OnClickListener getShareClickListener() {
		return shareClickListener;
	}

	public void setShareClickListener(OnClickListener shareClickListener) {
		this.shareClickListener = shareClickListener;
	}
	
	public UserQuoteWideItemView(Context context) {
	    super(context);
	}

	/**
	 * 
	 * @param userQuote
	 * @param id
	 * @param color
	 */
	public void bind(UserQuote userQuote, int color) {
		
		//Get the quote in certain language
		Quote quote = (Quote) userQuote.getRootQuote().getQuotes().toArray()[0];
    	
		//Typeface for Custom Font
    	Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/SEGOESC.TTF");


        if (buttonFavorite != null) {
            buttonFavorite.setFocusable(false);
            buttonFavorite.setTag(userQuote.getId());
            
            if(userQuote.isFavorited())
            	buttonFavorite.setImageResource( R.drawable.favorite_active );
            else
            	buttonFavorite.setImageResource( config.getFavoriteButton() );
            
            buttonFavorite.setOnClickListener(favoriteClickListener);
        }  
        if (buttonShare != null) {
            buttonShare.setFocusable(false); 
            buttonShare.setTag(userQuote.getId());
            buttonShare.setImageResource( config.getShareButton() );
            
            buttonShare.setOnClickListener(shareClickListener);
        }  
        
        if (textQuoteText != null) {
        	textQuoteText.setText( quote.getText() );  
        	textQuoteText.setTextColor( this.getContext().getResources().getColor( config.getForegroundColor()) );
        }            
        if (textAuthorFullName != null)  {
        	textAuthorFullName.setText( quote.getAuthorFullname() ); 
        	textAuthorFullName.setTextColor( this.getContext().getResources().getColor(config.getForegroundColor()) );
        	textAuthorFullName.setTypeface(tf,Typeface.NORMAL);
        }
        if (textCategoryDescription != null)  {
        	textCategoryDescription.setText( quote.getCategoryDescription() );
        	textCategoryDescription.setTextColor( this.getContext().getResources().getColor(config.getForegroundColor())  );
        	textCategoryDescription.setTypeface(tf,Typeface.NORMAL);
        }
        if (textReferenceDescription != null)  {
        	textReferenceDescription.setText( quote.getReferenceDescription() );  
        	textReferenceDescription.setTextColor( this.getContext().getResources().getColor(config.getForegroundColor()) );
        	textReferenceDescription.setTypeface(tf,Typeface.NORMAL);
        }


        if (quoteImage != null)  {
        	quoteImage.setImageResource( config.getQuoteMarkLeftImage() );
        }

        if (optionsContainer != null)  {
        	optionsContainer.setBackgroundResource( config.getBackgroundTabColor() );
        }

        if (quoteContainer != null)  {
        	quoteContainer.setBackgroundResource( color );
        }
	}
}
