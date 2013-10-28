package com.banlinea.control.entities.result;

import com.banlinea.control.entities.UserProfile;
import com.banlinea.control.remote.util.CallResult;

public class UserResult extends CallResult{
	
	


	private UserProfile Body;

	public UserProfile getBody() {
		return Body;
	}

	public void setBody(UserProfile body) {
		Body = body;
	}
	
	
}