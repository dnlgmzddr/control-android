package com.banlinea.control.bussiness;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Context;

import com.banlinea.control.entities.Category;
import com.banlinea.control.entities.Transaction;
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
	
	public List<UserBudget> getUserBudgets() {
		try {
			Dao<UserBudget,String> budgetDAO = this.getHelper().getBudgets();
			
			
			// EXPENSES
			List <Category> categories = new ArrayList<Category>();
			List <Category> parentCategories = new CategoryService(this.context).GetParentCategoriesPerGroup(Category.GROUP_EXPENSE);
			categories.addAll(parentCategories);
			for (Category parentCategory : parentCategories) {
				categories.addAll(new CategoryService(this.context).GetChilds(parentCategory.getId()));
			}
			
			for (Category category : categories) {
				List <UserBudget> budgets = budgetDAO.queryForEq("IdCategory", category.getId());
				List <Transaction> transactions = new TransactionService(this.context).getCurrentMonthTransactions();
			} 
			/*
			for (UserBudget userBudget : budgets) {
				float 
				for (Transaction transaction : transactions) {
					if (transaction.getIdCategory().equals(userBudget.setIdCategory())) {
						
					}
				}
			}*/
			 
			 return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
