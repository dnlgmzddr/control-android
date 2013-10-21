package com.banlinea.control.local;

import java.sql.SQLException;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.banlinea.control.R;
import com.banlinea.control.entities.UserProfile;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "bl.control.db";
	private static final int DATABASE_VERSION = 6;
	
	private Dao<UserProfile,String> userProfiles;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
	}
	
	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, UserProfile.class);
			
			
			UserProfile profile = new UserProfile();
			
			profile.setId("00000");
			profile.setName("Daniel");
			profile.setMail("dngmz");
			profile.setPassword("***");
			
			getDao(UserProfile.class).create(profile);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
		try {
			TableUtils.dropTable(connectionSource, UserProfile.class, true);
			onCreate(sqliteDatabase, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new "
					+ newVer, e);
		}
	}

	public Dao<UserProfile,String> getUserProfiles() throws SQLException {
		if(userProfiles == null){
			userProfiles = getDao(UserProfile.class);
		}
		return userProfiles;
	}

	

}
