package com.think.android.interceptor;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.support.HttpRequestWrapper;

/**
 * 
 * @author Rafael
 * Inclui Assinatura da aplicação na Request.
 */
public class SignatureHttpRequestWrapper extends HttpRequestWrapper {

	public String signature = "";
	public String additional = "";
	
	public String getAdditional() {
		return additional;
	}

	public void setAdditional(String additional) {
		this.additional = additional;
	}
	
	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public SignatureHttpRequestWrapper(HttpRequest request) {
		super(request);
	}
	
	@Override
	public URI getURI(){
		
		String target = super.getURI().toString();
		String complement = target.contains("?") ? "&" : "?";

		try {

			URI signedTarget = new URI( target + complement + additional + "&" + signature);
			
			return signedTarget;
			
		} catch (URISyntaxException e) {
			
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return super.getURI();
		}		
	}
}
