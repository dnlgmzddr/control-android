package com.banlinea.control.entities.result;

import com.banlinea.control.remote.util.CallResult;


public class LoginResult extends CallResult {

	
	private LoginData body;

	public LoginData getBody() {
		return body;
	}

	public void setBody(LoginData body) {
		this.body = body;
	}
	
	
	
	
}
