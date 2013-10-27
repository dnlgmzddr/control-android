package com.banlinea.control.remote;

import java.util.concurrent.ExecutionException;

import com.banlinea.control.entities.UserBudget;
import com.banlinea.control.remote.util.ApiMethod;
import com.banlinea.control.remote.util.CallResult;
import com.banlinea.control.remote.util.ControlApiHandler;

public class RemoteBudgetService {

	
	
	public CallResult AddUserBudget(UserBudget userBudget) throws InterruptedException, ExecutionException {
		
		CallResult result = new ControlApiHandler<CallResult,UserBudget>(
				userBudget,
				ApiMethod.BUDGET_ADD,
				CallResult.class
				).execute().get();
		
		return result;
	}



}
