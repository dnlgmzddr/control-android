package com.banlinea.control.entities.util;

import com.banlinea.control.entities.BaseEntity;

public class LoginRequest extends BaseEntity {

	private String userMail;
	private String password;

	
	
	public LoginRequest() {
		super();
	}

	public LoginRequest(String userMail, String password) {
		super();
		this.userMail = userMail;
		this.password = password;
	}

	public String getUserMail() {
		return userMail;
	}

	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
