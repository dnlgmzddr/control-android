package com.banlinea.control.remote.util;

/**
 * Represent an API method.
 */
public enum ApiMethod {

	/**
	 * Part of the authentication end point, this method create a new user.
	 */
	AUTH_CREATE_USER(HttpMethods.POST, "api/Auth/CreateUser"),

	/**
	 * Part of the authentication end point, this method authenticate a user
	 * with the given credentials.
	 */
	AUTH_LOGIN_USER(HttpMethods.POST, "api/Auth/Login"),

	/**
	 * Part of the budget SET UP, allow the user to configure his budget
	 */
	BUDGET_ADD(HttpMethods.POST, "api/Budget/AddBudget"),

	/**
	 * Part of the transaction engine, allow the user to add a transaction.
	 */
	TRNSACTION_ADD(HttpMethods.POST, "api/Transaction/Add"),

	/**
	 * Part of the category management, allow the user to add a custom category.
	 */
	CATS_ADD(HttpMethods.POST, "api/Categories/AddCustom"),
	
	/**
	 * Part of the category management service, this method get all the system
	 * categories.
	 */
	CATS_GET_ALL(HttpMethods.GET, "api/Categories/GetBasics"),

	PRODUCTS_GET_ADD_TO_USER(HttpMethods.POST,"api/FinancialProduct/AddProductToUser"),
	
	PRODUCTS_GET_FILTERED(HttpMethods.GET,"api/FinancialProduct/GetFiltered"),
	
	PRODUCTS_GET_USER_PROMOTIONS(HttpMethods.GET,"api/FinancialProduct/GetUserWeeklyPromotions"),
	
	/**
	 * Get the financial entities that have products for the specified category.
	 */
	PRODUCTS_GET_ENTITIES(HttpMethods.GET,"api/FinancialProduct/GetFinancialEntitiesByType");
	
	
	/**
	 * The HTTP method used to do the request. GET or POST
	 */
	private final HttpMethods httpMethod;

	/**
	 * URL of the service, after the domain.
	 */
	private final String url;

	ApiMethod(HttpMethods httpMethod, String url) {
		this.httpMethod = httpMethod;
		this.url = url;
	}

	/**
	 * Base URL for the API.
	 */
	private static final String ENDPOINT = "http://ec2-54-201-1-32.us-west-2.compute.amazonaws.com/";

	/**
	 * look up for the type of the given operation
	 * 
	 * @param operationName
	 *            name of the operation.
	 * @return 0 if post 1 if get.
	 */
	public HttpMethods getHttpMethod() {
		return this.httpMethod;
	}

	/**
	 * Build the full API URL.
	 * 
	 * @return the complete URL to do the request.
	 */
	public String buildUrl() {
		return ENDPOINT + this.url;
	}
}
