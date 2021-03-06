package com.banlinea.control.remote;

import java.util.concurrent.ExecutionException;

import com.banlinea.control.dto.out.LoginRequest;
import com.banlinea.control.entities.UserProfile;
import com.banlinea.control.entities.result.LoginResult;
import com.banlinea.control.entities.result.UserResult;
import com.banlinea.control.remote.util.ApiMethod;
import com.banlinea.control.remote.util.ControlApiHandler;

public class RemoteAuthenticationService {

	public UserResult Register(UserProfile userProfile) {
		UserResult result = null;

		try {

			result = new ControlApiHandler<UserResult, UserProfile>(
					userProfile, ApiMethod.AUTH_CREATE_USER, UserResult.class)
					.execute().get();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public LoginResult Auth(String email, String password) {
		LoginResult result = null;

		try {
			LoginRequest loginRequest = new LoginRequest(email,password);
			result = new ControlApiHandler<LoginResult, LoginRequest>(
					loginRequest, ApiMethod.AUTH_LOGIN_USER, LoginResult.class)
					.execute().get();

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
