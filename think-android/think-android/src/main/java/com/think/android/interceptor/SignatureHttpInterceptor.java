package com.think.android.interceptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import android.util.Base64;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;
import com.think.android.preference.UserConfiguration;

@EBean(scope = Scope.Singleton)
public class SignatureHttpInterceptor implements ClientHttpRequestInterceptor {

	@Bean
	UserConfiguration safe;
	
    Date currDate;
    SimpleDateFormat dateFormatGmt;
    SimpleDateFormat dateFormatLocal;
    
	public SignatureHttpInterceptor() {

	    dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss S");
	    dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
	    dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss S");
	}
	 
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] data, ClientHttpRequestExecution execution) throws IOException {
		//Elementos adicionais de verificação
		String add = getAdditional();
		//Gera Assinatura da URL
		String sign = getSignature(request.getURI(), add);
		
		SignatureHttpRequestWrapper signedRequest = new SignatureHttpRequestWrapper(request);
		signedRequest.setAdditional(add);
		signedRequest.setSignature( sign );
		
		//Continua com a requisição
		return execution.execute( signedRequest, data);
	}
	


	/**
	 * 
	 * @return
	 */
	private String getAdditional() {  

	    long currTime = 0;
	    try {
	        currDate = dateFormatLocal.parse( dateFormatGmt.format(new Date()) );
	        currTime = currDate.getTime();
	    } catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
        //Base String that will be encoded with HMAC
        String additional = "timestamp="+currTime+
                			"&apikey="+safe.getApiKey();       
        
        return additional;   
	}	 
	

	/**
	 * Gera a assinatura da Aplicação utilizando Secret Key
	 * @param uri 
	 * @return
	 */
	private String getSignature(URI uri, String addtional) {  
		
	    //Extract object requested
	    String obj = uri.toString().substring( safe.getEndPoint().length() );
	    
	    String join = (obj.contains("?") ? "&" : "?");
	    
        //Base String that will be encoded with HMAC
        String toBeSigned = obj + join + addtional;     
                
        javax.crypto.SecretKey key;		
        javax.crypto.Mac signer;

        byte[] hashedMessageBytes;
        String signatureString = "";
        
		try {
			key = new javax.crypto.spec.SecretKeySpec(safe.getPrivateKey().getBytes("UTF8"), "HmacSHA256");
			signer = javax.crypto.Mac.getInstance(key.getAlgorithm());
		    //Create Signature using HmacSHA256 
			signer.init(key);
	        //Remove whitespace from base string then get hmac
			hashedMessageBytes = signer.doFinal(toBeSigned.getBytes("UTF8"));
	        //Base64 encode Hashed key to String
	        signatureString = Base64.encodeToString(hashedMessageBytes,Base64.NO_WRAP);
	        
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     
        return "signature=" + signatureString;   
	}	 
}