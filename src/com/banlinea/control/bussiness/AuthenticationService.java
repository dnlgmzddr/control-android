package com.banlinea.control.bussiness;

import java.sql.SQLException;

import android.content.Context;

import com.banlinea.control.entities.UserProfile;
import com.banlinea.control.entities.util.CreateUserProfileResult;
import com.banlinea.control.remote.RemoteAuthenticationService;

public class AuthenticationService implements Disposable{

	
	private LocalConnectionManager localConnectionManeger;
	private RemoteAuthenticationService remoteAuthSerice;
	
	private Context activityContext;
	
	public AuthenticationService(Context context) {

		localConnectionManeger = new LocalConnectionManager();
		remoteAuthSerice = new RemoteAuthenticationService();
		activityContext = context;
	}
	
	public void Register(UserProfile userProfile) throws SQLException{
		CreateUserProfileResult result = remoteAuthSerice.Register(userProfile);
		if(result.isSuccessfullOperation()){
			localConnectionManeger.getHelper(activityContext).getUserProfiles().create(result.getBody());
		}
			
	}
	
	@Override
	public void onDestroy() {
		localConnectionManeger.onDestroy();
		
	}

}
