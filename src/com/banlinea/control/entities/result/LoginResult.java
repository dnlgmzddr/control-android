package com.banlinea.control.entities.result;

import com.banlinea.control.remote.util.CallResult;


public class LoginResult extends CallResult {

	
	private LoginData Body;

	public LoginData getBody() {
		return Body;
	}

	public void setBody(LoginData body) {
		Body = body;
	}
	
	
	
	
}
