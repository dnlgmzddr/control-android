package com.banlinea.control.bussiness;

import android.content.Context;

import com.banlinea.control.entities.UserBudget;
import com.banlinea.control.remote.RemoteBudgetService;

public class BudgetService extends BaseService {
	
	
	private RemoteBudgetService remoteBudgetService;

	public BudgetService(Context context) {

		super(context);
		remoteBudgetService = new RemoteBudgetService();
	}
	
	public boolean AddBudget(UserBudget userBudget){
		
		return false;
	}
}
