package com.banlinea.control.bussiness;

import java.sql.SQLException;

import android.content.Context;

import com.banlinea.control.entities.UserProfile;
import com.banlinea.control.entities.util.CreateUserProfileResult;
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
		CreateUserProfileResult result = remoteAuthSerice.Register(userProfile);
		if (result != null && result.isSuccessfullOperation()) {
			
			DatabaseHelper helper = this.getHelper();
			Dao<UserProfile, String> dao = helper.getUserProfiles();
			UserProfile profileToSave = result.getBody();
			dao.create(profileToSave);
		}

	}



}
