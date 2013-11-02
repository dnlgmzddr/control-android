package com.banlinea.control.bussiness;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import android.content.Context;

import com.banlinea.control.entities.UserBudget;
import com.banlinea.control.remote.RemoteBudgetService;
import com.banlinea.control.remote.util.CallResult;
import com.j256.ormlite.dao.Dao;

public class BudgetService extends BaseService {
	
	
	private RemoteBudgetService remoteBudgetService;

	public BudgetService(Context context) {

		super(context);
		remoteBudgetService = new RemoteBudgetService();
	}
	
	/**
	 * Add or update a budget for a user.
	 * @param userBudget
	 * @return
	 */
	public CallResult AddBudget(UserBudget userBudget){
		CallResult callResult = null;
		try {
			callResult = remoteBudgetService.AddUserBudget(userBudget);
			if(callResult.isSuccessfullOperation()){
				Dao<UserBudget,String> budgets =  this.getHelper().getBudgets();
				budgets.createOrUpdate(userBudget);
			}
		} catch (InterruptedException e) {
			callResult = new CallResult();
			callResult.setMessage(e.getMessage());
			callResult.setSuccessfullOperation(false);
		} catch (ExecutionException e) {
			callResult = new CallResult();
			callResult.setMessage(e.getMessage());
			callResult.setSuccessfullOperation(false);
		} catch (SQLException e) {
			callResult = new CallResult();
			callResult.setMessage(e.getMessage());
			callResult.setSuccessfullOperation(false);
		}
		return callResult;
		
	}
	

}
