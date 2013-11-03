package com.banlinea.control.entities.result;

import com.banlinea.control.dto.in.LoginDTO;
import com.banlinea.control.remote.util.CallResult;


public class LoginResult extends CallResult {

	
	private LoginDTO body;

	public LoginDTO getBody() {
		return body;
	}

	public void setBody(LoginDTO body) {
		this.body = body;
	}
	
	
	
	
}
