package com.banlinea.control.remote;

import java.util.concurrent.ExecutionException;

import com.banlinea.control.entities.BaseEntity;
import com.banlinea.control.entities.UserBudget;
import com.banlinea.control.entities.util.GetAllBasicCateoriesResult;
import com.banlinea.control.remote.util.ApiMethod;
import com.banlinea.control.remote.util.CallResult;
import com.banlinea.control.remote.util.ControlApiHandler;

public class RemoteBudgetService {

	
	
	public CallResult AddUserBudget(UserBudget userBudget) throws InterruptedException, ExecutionException {
		
		new ControlApiHandler<GetAllBasicCateoriesResult,BaseEntity>(
				null,
				ApiMethod.CATS_GET_ALL,
				GetAllBasicCateoriesResult.class
				).execute().get();
		
		return null;
	}



}
