package com.banlinea.control.bussiness;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.banlinea.control.entities.UserProfile;
import com.banlinea.control.entities.util.UserResult;
import com.banlinea.control.local.DatabaseHelper;
import com.banlinea.control.remote.RemoteAuthenticationService;
import com.banlinea.control.remote.util.CallResult;
import com.j256.ormlite.dao.Dao;

public class AuthenticationService extends BaseService {

	private RemoteAuthenticationService remoteAuthSerice;

	public AuthenticationService(Context context) {

		super(context);
		remoteAuthSerice = new RemoteAuthenticationService();
	}

	public CallResult Register(UserProfile userProfile) throws SQLException {
		UserResult result = remoteAuthSerice.Register(userProfile);
		if (result != null && result.isSuccessfullOperation()) {

			DatabaseHelper helper = this.getHelper();
			Dao<UserProfile, String> dao = helper.getUserProfiles();
			UserProfile profileToSave = result.getBody();
			dao.create(profileToSave);
		}
		return result;

	}

	public CallResult Login(String userMail, String password) throws SQLException {
		
		UserResult result = remoteAuthSerice.Auth(userMail, password);
		
		if (result != null && result.isSuccessfullOperation()) {
			
			DatabaseHelper helper = this.getHelper();
			Dao<UserProfile, String> dao = helper.getUserProfiles();
			UserProfile profileToSave = result.getBody()	;
			dao.create(profileToSave);
		}
		
		return result;
	}
	
	public void Logout() throws SQLException {
		DatabaseHelper helper = this.getHelper();
		Dao<UserProfile, String> dao = helper.getUserProfiles();
		dao.delete(dao.queryForAll());
	}

	public boolean isLoggedIn() throws SQLException {
		DatabaseHelper helper = this.getHelper();
		Dao<UserProfile, String> dao = helper.getUserProfiles();
		return dao.countOf() > 0;
	}
	
	public UserProfile GetUser() throws SQLException {
		DatabaseHelper helper = this.getHelper();
		Dao<UserProfile, String> dao = helper.getUserProfiles();
		List<UserProfile> users = dao.queryForAll();
		if (users.size() == 0) { 
			return null;
		}
		else {
			return users.get(0);
		}
	}

}
