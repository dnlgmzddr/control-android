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
	 * 
	 * @param userBudget
	 * @return
	 */
	public CallResult AddBudget(UserBudget userBudget) {
		CallResult callResult = null;
		try {
			callResult = remoteBudgetService.AddUserBudget(userBudget);
			if (callResult.isSuccessfullOperation()) {
				Dao<UserBudget, String> budgets = this.getHelper().getBudgets();
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

	public List<UserBudget> getUserBudgets(int groupId) {
		List<UserBudget> result = new ArrayList<UserBudget>();
		try {
			Dao<UserBudget, String> budgetDAO = this.getHelper().getBudgets();

			List<Category> categories = new ArrayList<Category>();
			List<Category> parentCategories = new CategoryService(this.context)
					.GetParentCategoriesPerGroup(groupId);
			categories.addAll(parentCategories);
			for (Category parentCategory : parentCategories) {
				categories.addAll(new CategoryService(this.context)
						.GetChilds(parentCategory.getId()));
			}

			for (Category category : categories) {
				UserBudget budget = budgetDAO.queryForId(category.getId());
				if (budget != null) {
					List<Transaction> transactions = new TransactionService(
							this.context).getCurrentMonthTransactionsFor(category
							.getId());
	
					float executedBudget = 0;
	
					for (Transaction transaction : transactions) {
						executedBudget += (float) transaction.getAmount();
					}
					
					budget.setCurrentExecutedBudget(executedBudget);
					result.add(budget);
				}
			}

			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
