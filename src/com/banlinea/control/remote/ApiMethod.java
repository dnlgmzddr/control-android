package com.banlinea.control.remote;


public enum ApiMethod {

	AUTH_CREATE_USER(HttpMethods.POST, "api/Auth/CreateUser");

	private final HttpMethods httpMethod;
	
	private final String url;
	
	
	ApiMethod(HttpMethods httpMethod, String url){
		this.httpMethod = httpMethod;
		this.url = url;
	}
	
	private static final String ENDPOINT ="http://controlbanlinea.azurewebsites.net/";
	

	
	/**
	 * look up for the type of the given operation
	 * @param operationName name of the operation.
	 * @return 0 if post 1 if get.
	 */
	public HttpMethods getHttpMethod(){
		return this.httpMethod;
	}
	
	public String buildUrl(){
		return ENDPOINT + this.url;
	}
}
