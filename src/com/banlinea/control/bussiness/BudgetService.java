package com.banlinea.control.bussiness;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Context;

import com.banlinea.control.entities.Category;
import com.banlinea.control.entities.Transaction;
import com.banlinea.control.entities.UserBudget;
import com.banlinea.control.remote.RemoteBudgetService;
import com.banlinea.control.remote.util.CallResult;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

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
							this.context)
							.getCurrentMonthTransactionsFor(category.getId());

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

	public float getDailyBudget() {
		float dailyBudget = 0f;

		float monthlyIncome = calculateMonthlyIncome();
		
		float fixedExpenses = getFixedCategoriesBudget();

		 float expensesDueDate =  new TransactionService(this.context).getExpensesDueDate();
		
		dailyBudget = monthlyIncome - fixedExpenses - expensesDueDate;
		
		int days = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
		
		
		
		dailyBudget = dailyBudget/days;
		
		return dailyBudget;
	}


	private float getFixedCategoriesBudget() {
		float fixedExpenses = 0f;
		try {

			
			Dao<UserBudget, String> budgetDao = this.getHelper().getBudgets();

			
			List<String> incomeCategoriesIds = new CategoryService(this.context).getFixedExpensesCategoriesIds();

			QueryBuilder<UserBudget, String> budgetQBuilder = budgetDao
					.queryBuilder();

			budgetQBuilder.where().in("idCategory", incomeCategoriesIds);

			PreparedQuery<UserBudget> budgetQuery = budgetQBuilder.prepare();

			List<UserBudget> budgets = budgetDao.query(budgetQuery);

			for (UserBudget expenseBudget : budgets) {
				fixedExpenses += Math.max(expenseBudget.getBudget(), new TransactionService(this.context).getTopTransactionInCurrentMonth(expenseBudget.getIdCategory()));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fixedExpenses;
	}

	

	public float calculateMonthlyIncome() {
		float monthlyIncome = 0f;
		try {

			
			Dao<UserBudget, String> budgetDao = this.getHelper().getBudgets();

			List<String> incomeCategoriesIds = new CategoryService(this.context).getIncomeCategoriesId();

			QueryBuilder<UserBudget, String> budgetQBuilder = budgetDao
					.queryBuilder();

			budgetQBuilder.where().in("idCategory", incomeCategoriesIds);

			PreparedQuery<UserBudget> budgetQuery = budgetQBuilder.prepare();

			List<UserBudget> budgets = budgetDao.query(budgetQuery);

			for (UserBudget incomeBudget : budgets) {
				monthlyIncome += incomeBudget.getBudget();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return monthlyIncome;
	}

	

}
