package com.banlinea.control.entities.util;

import java.util.Date;

import android.text.format.DateFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class GsonProvider {
	
	public static Gson getGson(){
		GsonBuilder builder = new GsonBuilder();
		builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		builder.registerTypeAdapter(Date.class, new ISODateAdapter());
		return builder.create();
	}

}