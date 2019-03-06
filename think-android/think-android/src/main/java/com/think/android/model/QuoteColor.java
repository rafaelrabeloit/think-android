package com.think.android.model;

import br.com.think.model.ModelBase;

public class QuoteColor extends ModelBase {

	private Integer userQuoteId;
	private Integer colorRes;

	public QuoteColor() { }

	public QuoteColor(Integer userQuoteId, Integer colorRes) { 
		this.userQuoteId = userQuoteId;
		this.colorRes = colorRes;
	}
	
	@Override
	public Integer getId() {
		return this.userQuoteId;
	}
	
	@Override
	public void setId(Integer id) {
		this.userQuoteId = id;
	}
	
	public Integer getUserQuoteId() {
		return userQuoteId;
	}

	public void setUserQuoteId(Integer userQuoteId) {
		this.userQuoteId = userQuoteId;
	}

	public Integer getColorRes() {
		return colorRes;
	}

	public void setColorRes(Integer colorRes) {
		this.colorRes = colorRes;
	}

}
