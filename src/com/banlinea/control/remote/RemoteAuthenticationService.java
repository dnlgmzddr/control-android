package com.banlinea.control.remote;

import java.util.concurrent.ExecutionException;

import com.banlinea.control.entities.UserProfile;
import com.banlinea.control.entities.util.CreateUserProfileResult;
import com.banlinea.control.remote.util.ApiMethod;

public class RemoteAuthenticationService {

	public CreateUserProfileResult Register(UserProfile userProfile){
		CreateUserProfileResult result = null;
		
		try {
			
			result = new ControlApiHandler<CreateUserProfileResult, UserProfile>(
						userProfile,
						ApiMethod.AUTH_CREATE_USER,
						CreateUserProfileResult.class
						).execute().get();
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	
	
}
