package com.banlinea.control.remote;

/**
 * Represent an API method.
 */
public enum ApiMethod {

	/**
	 * Part of the authentication end point, this method create a new user.
	 */
	AUTH_CREATE_USER(HttpMethods.POST, "api/Auth/CreateUser");

	/**
	 * The HTTP method used to do the request. 
	 * GET or POST
	 */
	private final HttpMethods httpMethod;
	
	/**
	 * URL of the service, after the domain.
	 */
	private final String url;
	
	
	ApiMethod(HttpMethods httpMethod, String url){
		this.httpMethod = httpMethod;
		this.url = url;
	}
	
	/**
	 * Base URL for the API.
	 */
	private static final String ENDPOINT ="http://controlbanlinea.azurewebsites.net/";
	

	
	/**
	 * look up for the type of the given operation
	 * @param operationName name of the operation.
	 * @return 0 if post 1 if get.
	 */
	public HttpMethods getHttpMethod(){
		return this.httpMethod;
	}
	
	/**
	 * Build the full API URL.
	 * @return the complete URL to do the request.
	 */
	public String buildUrl(){
		return ENDPOINT + this.url;
	}
}
