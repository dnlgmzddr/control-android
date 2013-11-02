package com.banlinea.control.entities.result;

import com.banlinea.control.entities.UserProfile;
import com.banlinea.control.remote.util.CallResult;

public class UserResult extends CallResult{
	
	


	private UserProfile body;

	public UserProfile getBody() {
		return body;
	}

	public void setBody(UserProfile body) {
		this.body = body;
	}
	
	
}