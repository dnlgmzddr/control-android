package com.banlinea.control.remote;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.banlinea.control.remote.entities.BaseEntity;
import com.google.gson.Gson;

public class ControlApiHandler {

	/**
	 * 
	 * @param method
	 * @param parameter
	 * @param destinyClass
	 * @return Something of type T
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static <T, V extends BaseEntity> T doRequest(ApiMethod method,
			V parameter, Class<T> destinyClass) throws ClientProtocolException,
			IOException {
		String plainObject = innerDoRequest(method, parameter);
		Gson gson = new Gson();
		return gson.fromJson(plainObject, destinyClass);
	}

	public static <V extends BaseEntity> JSONObject doRequest(ApiMethod method,
			V parameter) throws ClientProtocolException, IOException,
			JSONException {
		String plainObject = innerDoRequest(method, parameter);
		return new JSONObject(plainObject);
	}

	public static <T> T doRequest(ApiMethod method,
			ArrayList<BasicNameValuePair> parameters, Class<T> destinyClass)
			throws ClientProtocolException, IOException {
		String plainObject = innerDoRequest(method, parameters);
		Gson gson = new Gson();
		return gson.fromJson(plainObject, destinyClass);
	}

	public static JSONObject doRequest(ApiMethod method,
			ArrayList<BasicNameValuePair> parameters)
			throws ClientProtocolException, IOException, JSONException {
		String plainObject = innerDoRequest(method, parameters);
		return new JSONObject(plainObject);
	}

	private static String innerDoRequest(ApiMethod method,
			ArrayList<BasicNameValuePair> parameters) throws IOException,
			ClientProtocolException {
		String plainObject = null;

		switch (method.getHttpMethod()) {
		case POST:
			plainObject = doPostRequest(method, parameters);
			break;

		case GET:
			plainObject = doGetRequest(method, parameters);
			break;
		}
		return plainObject;
	}

	private static <V extends BaseEntity> String innerDoRequest(
			ApiMethod method, V parameter) throws IOException,
			ClientProtocolException {
		String plainObject = null;

		switch (method.getHttpMethod()) {
		case POST:
			plainObject = doPostRequest(method, parameter);
			break;

		case GET:
			plainObject = doGetRequest(method, parameter);
			break;
		}
		return plainObject;
	}

	private static String doGetRequest(ApiMethod method,
			ArrayList<BasicNameValuePair> parameters) throws IOException,
			ClientProtocolException {

		HttpGet request = new HttpGet(method.buildUrl());

		BasicHttpParams httpParams = new BasicHttpParams();
		for (BasicNameValuePair pair : parameters) {
			httpParams.setParameter(pair.getName(), pair.getValue());
		}
		request.setParams(httpParams);
		return innerApiCall(request);
	}

	private static String doPostRequest(ApiMethod method,
			ArrayList<BasicNameValuePair> parameters) throws IOException,
			ClientProtocolException {

		HttpPost request = new HttpPost(method.buildUrl());

		request.setEntity(new UrlEncodedFormEntity(parameters));
		return innerApiCall(request);
	}

	private static <V extends BaseEntity> String doGetRequest(ApiMethod method,
			V parameter) throws IOException, ClientProtocolException {

		HttpGet request = new HttpGet(method.buildUrl());

		BasicHttpParams httpParams = new BasicHttpParams();
		for (Field field : parameter.getClass().getDeclaredFields()) {
			try {
				httpParams.setParameter(field.getName(), field.get(parameter));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		request.setParams(httpParams);

		return innerApiCall(request);
	}

	private static <V extends BaseEntity> String doPostRequest(
			ApiMethod method, V parameter) throws IOException,
			ClientProtocolException {

		HttpPost request = new HttpPost(method.buildUrl());
		request.addHeader("Content-Type", "application/json");
		Gson gson = new Gson();
		request.setEntity(new StringEntity(gson.toJson(parameter)));
		return innerApiCall(request);
	}

	private static String innerApiCall(HttpUriRequest request)
			throws IOException, ClientProtocolException {
		HttpClient client = new DefaultHttpClient();
		request.addHeader("Accept", "application/json");
		HttpResponse response = client.execute(request);
		String plainObject = EntityUtils.toString(response.getEntity());
		return plainObject;
	}

}
