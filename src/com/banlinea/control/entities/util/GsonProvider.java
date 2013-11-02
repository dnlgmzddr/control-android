package com.banlinea.control.entities.util;

import java.util.Date;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class GsonProvider {
	
	public static Gson getGson(){
		GsonBuilder builder = new GsonBuilder();
		builder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
		builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		builder.registerTypeAdapter(Date.class, new ISODateAdapter());
		return builder.create();
	}

}