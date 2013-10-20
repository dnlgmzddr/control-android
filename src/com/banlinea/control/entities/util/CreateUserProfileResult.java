package com.banlinea.control.entities.util;

import com.banlinea.control.entities.UserProfile;
import com.banlinea.control.remote.CallResult;

public class CreateUserProfileResult extends CallResult{
	private UserProfile Body;

	public UserProfile getBody() {
		return Body;
	}

	public void setBody(UserProfile body) {
		Body = body;
	}
	
	
}