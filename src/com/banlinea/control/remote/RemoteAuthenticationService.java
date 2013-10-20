package com.banlinea.control.remote;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.banlinea.control.entities.UserProfile;
import com.banlinea.control.entities.util.CreateUserProfileResult;

public class RemoteAuthenticationService {

	public CreateUserProfileResult Register(UserProfile userProfile){
		CreateUserProfileResult result = null;
		
		try {
			result = ControlApiHandler.doRequest(ApiMethod.AUTH_CREATE_USER, userProfile, CreateUserProfileResult.class);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	
	
}
