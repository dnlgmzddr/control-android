package com.banlinea.control.remote.util;

import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

public class ControlApiHandler<T, V> extends
		AsyncTask<Void, Void, T> {

	private V requestObject;
	private ApiMethod method;

	private Class<T> targetResponseClass;

	/**
	 * Handler for remote API calls.
	 * 
	 * @param requestObject
	 *            the request object to be used.
	 * @param method
	 *            The API method to be invoked
	 * @param targetResponseClass
	 *            identify the object who will fit the server response.
	 */
	public ControlApiHandler(V requestObject, ApiMethod method,
			Class<T> targetResponseClass) {
		this.requestObject = requestObject;
		this.method = method;
		this.targetResponseClass = targetResponseClass;
	}

	/**
	 * 
	 * @param method
	 * @param parameter
	 * @param destinyClass
	 * @return Something of type T
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private T doRequest() throws ClientProtocolException, IOException {
		String plainObject = innerDoRequest(method, requestObject);
		Log.d("REMOTE_RESPONSE", plainObject);
		Gson gson = GsonProvider.getGson();
		return gson.fromJson(plainObject, targetResponseClass);
	}

	private String innerDoRequest(ApiMethod method, V parameter)
			throws IOException, ClientProtocolException {
		String plainObject = null;

		switch (method.getHttpMethod()) {
		case POST:
			plainObject = doPostRequest();
			break;

		case GET:
			plainObject = doGetRequest();
			break;
		}
		return plainObject;
	}

	private String doGetRequest() throws IOException, ClientProtocolException {

		HttpGet request = new HttpGet(method.buildUrl());

		BasicHttpParams httpParams = new BasicHttpParams();
		if (requestObject != null) {
			for (Field field : requestObject.getClass().getDeclaredFields()) {
				try {
					httpParams.setParameter(field.getName(),
							field.get(requestObject));

				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		request.addHeader("Accept", "application/json");
		request.setParams(httpParams);

		return innerApiCall(request);
	}

	private String doPostRequest() throws IOException, ClientProtocolException {
		HttpPost request = new HttpPost(method.buildUrl());
		request.addHeader("Content-Type", "application/json");
		request.addHeader("Accept", "application/json");
		Gson gson = GsonProvider.getGson();
		String rawBody = gson.toJson(requestObject);
		Log.d("REMOTE_DO_POST", rawBody);
		request.setEntity(new StringEntity(rawBody));

		StringBuilder sb = new StringBuilder();

		Header[] headers = request.getAllHeaders();
		for (Header header : headers) {
			sb.append(header.getName() + ":: " + header.getValue() + "::");
		}
	
		return innerApiCall(request);
	}

	private static String innerApiCall(HttpUriRequest request)
			throws IOException, ClientProtocolException {

		HttpClient client = new DefaultHttpClient();

		HttpResponse response = client.execute(request);
		String plainObject = EntityUtils.toString(response.getEntity());
		return plainObject;
	}

	@Override
	protected T doInBackground(Void... params) {
		try {
			return this.doRequest();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
