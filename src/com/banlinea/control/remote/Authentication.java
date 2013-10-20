package com.banlinea.control.remote;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.banlinea.control.remote.entities.UserProfile;

public class Authentication {

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
	
	
	class CreateUserProfileResult extends CallResult{
		private UserProfile Body;

		public UserProfile getBody() {
			return Body;
		}

		public void setBody(UserProfile body) {
			Body = body;
		}
		
		
	}
	
}
