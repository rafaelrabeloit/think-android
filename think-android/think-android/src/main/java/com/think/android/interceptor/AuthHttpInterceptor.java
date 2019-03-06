package com.think.android.interceptor;

import java.io.IOException;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;



import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;
import com.think.android.preference.UserConfiguration;

@EBean(scope = Scope.Singleton)
public class AuthHttpInterceptor implements ClientHttpRequestInterceptor {

	@Bean
	UserConfiguration authStore;
	
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
	    
		HttpHeaders headers = request.getHeaders();
		
	    HttpAuthentication auth = new HttpBasicAuthentication(authStore.getUsername(), authStore.getPassword());
	    headers.setAuthorization(auth);
	    
	    return execution.execute(request, body);
	}
}