package com.think.android.rest;

import org.springframework.web.client.RestTemplate;

import br.com.think.model.Device;
import br.com.think.model.Language;
import br.com.think.model.User;
import br.com.think.model.UserQuote;

import com.googlecode.androidannotations.annotations.rest.Accept;
import com.googlecode.androidannotations.annotations.rest.Get;
import com.googlecode.androidannotations.annotations.rest.Post;
import com.googlecode.androidannotations.annotations.rest.Put;
import com.googlecode.androidannotations.annotations.rest.Rest;
import com.googlecode.androidannotations.api.rest.MediaType;

/**
 * Interface used by Android Annotations to make RESTful requests. And the URL endpoint to every request.
 * @author Rafael
 */
@Rest(converters = { CustomHttpMessageConverter.class })
@Accept(MediaType.APPLICATION_JSON)
public interface RestInterface {

	/**
	 * Verify if a specified lang exists on service
	 * @param langCode
	 * @return
	 */
	@Get("/languages/{langCode}")
	Language getLanguage(String langCode);

	/**
	 * Get all supported languages
	 * @return
	 */
	@Get("/languages")
	Language[] getAllLanguages();
	
	/**
	 * Get all UserQuotes
	 * @param langId
	 * @param userId
	 * @return
	 */
	@Get("/{langId}/users/{userId}/userquotes")
	UserQuote[] getAllUserQuotes(long langId, long userId);
	
	/**
	 * Get the 'recent' UserQuotes
	 * @param langId
	 * @param userId
	 * @param after
	 * @return
	 */
	@Get("/{langId}/users/{userId}/userquotes?after={after}")
	UserQuote[] getRecentsUserQuotes(long langId, long userId, long after);

	/**
	 * Get the 'recent' UserQuotes
	 * @param langId
	 * @param userId
	 * @return
	 */
	@Get("/{langId}/users/{userId}/userquotes/random")
	UserQuote getRandomUserQuote(long langId, long userId);
	
	/**
	 * Update the UserQuote
	 * @param userQuote
	 * @param langId
	 * @param userId
	 * @param userQuotesId
	 */
	@Put("/{langId}/users/{userId}/userquotes/{userQuotesId}")
	void editUserQuote(UserQuote userQuote, long langId, long userId, long userQuotesId);

	/**
	 * Get the user configurations (because may changed)
	 * @param userId
	 * @return
	 */
	@Get("/users/{userId}")
	User getUser(long userId);
	
	/**
	 * Add a new User, must get the result and STORE!
	 * @param user
	 * @return
	 */
	@Post("/users")
	User addUser(User user);
	
	/**
	 * Change the user settings
	 * @param user
	 * @param userId
	 */
	@Put("/users/{userId}")
	void editUser(User user, long userId);

	/**
	 * Get the device configurations (because may changed)
	 * @param langId
	 * @param userId
	 * @param deviceId
	 * @return
	 */
	@Get("/{langId}/users/{userId}/device/{deviceId}")
	Device getDevice(long langId, long userId, long deviceId);

	/**
	 * Add a Device to the user
	 * @param device
	 * @param langId
	 * @param userId
	 * @return
	 */
	@Post("/{langId}/users/{userId}/device")
	Device addDevice(Device device, long langId, long userId);
	
	/**
	 * Change the device settings
	 * @param device
	 * @param langId
	 * @param userId
	 * @param deviceId
	 */
	@Put("/{langId}/users/{userId}/device/{deviceId}")
	void editDevice(Device device, long langId, long userId, long deviceId);

	/**
	 * 
	 */
	void setRootUrl(String rootUrl);
	
	/**
	 * 
	 * @return
	 */
	RestTemplate getRestTemplate();
	
	/**
	 * 
	 * @param restTemplate
	 */
	void setRestTemplate(RestTemplate restTemplate);	
}