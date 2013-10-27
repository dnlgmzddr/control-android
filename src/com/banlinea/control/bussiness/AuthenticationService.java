package com.banlinea.control.bussiness;

import java.sql.SQLException;

import android.content.Context;

import com.banlinea.control.entities.UserProfile;
import com.banlinea.control.entities.util.UserResult;
import com.banlinea.control.local.DatabaseHelper;
import com.banlinea.control.remote.RemoteAuthenticationService;
import com.j256.ormlite.dao.Dao;

public class AuthenticationService extends BaseService {

	private RemoteAuthenticationService remoteAuthSerice;

	public AuthenticationService(Context context) {

		super(context);
		remoteAuthSerice = new RemoteAuthenticationService();
	}

	public void Register(UserProfile userProfile) throws SQLException {
		UserResult result = remoteAuthSerice.Register(userProfile);
		if (result != null && result.isSuccessfullOperation()) {

			DatabaseHelper helper = this.getHelper();
			Dao<UserProfile, String> dao = helper.getUserProfiles();
			UserProfile profileToSave = result.getBody();
			dao.create(profileToSave);
		}

	}

	public boolean Login(String userMail, String password) throws SQLException {
		boolean sucess = false;
		UserResult result = remoteAuthSerice.Auth(userMail, password);
		
		if (result != null && result.isSuccessfullOperation()) {
			sucess = true;
			DatabaseHelper helper = this.getHelper();
			Dao<UserProfile, String> dao = helper.getUserProfiles();
			UserProfile profileToSave = result.getBody()	;
			dao.create(profileToSave);
		}
		
		return sucess;
	}

}
