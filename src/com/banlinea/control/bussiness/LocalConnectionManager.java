package com.banlinea.control.bussiness;

import android.content.Context;

import com.banlinea.control.local.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class LocalConnectionManager implements Disposable{
	
	private DatabaseHelper databaseHelper = null;
	
	
	public DatabaseHelper getHelper(Context context) {
	    if (databaseHelper == null) {
	        databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
	    }
	    return databaseHelper;
	}
	
	/* (non-Javadoc)
	 * @see com.banlinea.control.bussiness.Disposable#onDestroy()
	 */
	@Override
	public void onDestroy() {

		/*
		 * You'll need this in your class to release the helper when done.
		 */
		if (databaseHelper != null) {
			databaseHelper.close();
			databaseHelper = null;
		}
	}
}
