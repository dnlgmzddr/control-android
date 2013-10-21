package com.banlinea.control.bussiness;

import android.content.Context;

import com.banlinea.control.local.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class BaseService {
	private DatabaseHelper databaseHelper = null;

	private Context context;
	
	public BaseService(Context context){
		this.context = context;
	}
	
	protected BaseService(){
		
	}
	
	protected DatabaseHelper getHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(this.context,
					DatabaseHelper.class);
		}
		return databaseHelper;
	}
}
