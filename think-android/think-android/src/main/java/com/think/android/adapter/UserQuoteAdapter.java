package com.think.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import br.com.think.model.UserQuote;
import com.think.android.view.UserQuoteSquareItemView_;
import com.think.android.view.UserQuoteWideItemView_;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.think.android.model.QuoteColor;
import com.think.android.util.ModelSparseArray;
import com.think.android.view.UserQuoteSquareItemView;
import com.think.android.view.UserQuoteWideItemView;

@EBean
public class UserQuoteAdapter extends ModelAdapter<UserQuote> {

	@RootContext
    Context context;
    
	private ModelSparseArray<QuoteColor> quoteColors;

	private OnClickListener favoriteClickListener = null;

	private OnClickListener shareClickListener = null;
	
	private boolean wide = true;

	public void setWide(boolean wide) {
		this.wide = wide;
	}

	public void setFavoriteClickListener(OnClickListener favoriteClickListener) {
		this.favoriteClickListener = favoriteClickListener;
	}

	public void setShareClickListener(OnClickListener shareClickListener) {
		this.shareClickListener = shareClickListener;
	}

	public void setColorQuotes(ModelSparseArray<QuoteColor> quoteColors) {
		this.quoteColors = quoteColors;
	}
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	 	
    	if(wide){
	    	UserQuoteWideItemView wideItemView;
	        if (convertView == null) {
	            wideItemView = UserQuoteWideItemView_.build(context);
	        } else {
	            wideItemView = (UserQuoteWideItemView) convertView;
	        }
	
	        UserQuote userQuote = getItem(position);
	        QuoteColor color = quoteColors.getByKey(userQuote.getId());
	        
	        wideItemView.setShareClickListener(shareClickListener);
	        wideItemView.setFavoriteClickListener(favoriteClickListener);
	        
	        wideItemView.bind( userQuote, color.getColorRes() );
	
	        return wideItemView;
    	}
    	else{
    		UserQuoteSquareItemView squareItemView;
	        if (convertView == null) {
	        	squareItemView = UserQuoteSquareItemView_.build(context);
	        } else {
	        	squareItemView = (UserQuoteSquareItemView) convertView;
	        }
	
	        UserQuote userQuote = getItem(position);
	        QuoteColor color = quoteColors.get(userQuote.getId());
	        	        
	        squareItemView.bind( userQuote, color.getColorRes(), false );
	
	        return squareItemView;
    	}
    }
}