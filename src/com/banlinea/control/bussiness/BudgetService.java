package com.banlinea.control.bussiness;

import java.util.concurrent.ExecutionException;

import android.content.Context;

import com.banlinea.control.entities.UserBudget;
import com.banlinea.control.remote.RemoteBudgetService;
import com.banlinea.control.remote.util.CallResult;

public class BudgetService extends BaseService {
	
	
	private RemoteBudgetService remoteBudgetService;

	public BudgetService(Context context) {

		super(context);
		remoteBudgetService = new RemoteBudgetService();
	}
	
	public CallResult AddBudget(UserBudget userBudget) throws InterruptedException, ExecutionException{
		CallResult callResult = remoteBudgetService.AddUserBudget(userBudget);
		return callResult;
	}
}
