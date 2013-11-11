package com.banlinea.control.bussiness;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.util.Log;

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

	private TransactionService transactionService;

	public BudgetService(Context context) {

		super(context);
		remoteBudgetService = new RemoteBudgetService();
		transactionService = new TransactionService(this.context);
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

	public UserBudget getUserBudget(String idCategory) {
		Dao<UserBudget, String> budgetDAO;
		UserBudget userBudget = null;
		try {
			budgetDAO = this.getHelper().getBudgets();
			CategoryService catService = new CategoryService(this.context);
			Category cat = catService.GetCategory(idCategory);
			userBudget = new UserBudget();
			userBudget.setIdUser(new AuthenticationService(this.context)
					.GetUser().getId());
			userBudget.setIdCategory(idCategory);

			if (cat.getIdParent().equals(Category.SYSTEM_EMPTY_ID)) {

				List<Category> childrens = catService.GetChilds(idCategory);
				for (Category child : childrens) {
					UserBudget childBudget = this.getUserBudget(child.getId());
					if (childBudget == null) {
						continue;
					}

					userBudget.setBudget(userBudget.getBudget()
							+ childBudget.getBudget());

					userBudget.setCurrentExecutedBudget(userBudget
							.getCurrentExecutedBudget()
							+ childBudget.getCurrentExecutedBudget());
				}

			} else {
				userBudget = budgetDAO.queryForId(idCategory);
				if (userBudget == null) {
					userBudget = new UserBudget();
					userBudget
							.setIdUser(new AuthenticationService(this.context)
									.GetUser().getId());
					userBudget.setIdCategory(idCategory);
					userBudget.setBudget(0);
				}
				TransactionService transactionService = new TransactionService(
						this.context);
				List<Transaction> transactions = transactionService
						.getCurrentMonthTransactionsFor(idCategory);
				float executed = 0f;
				for (Transaction transaction : transactions) {
					executed += transaction.getAmount();
				}
				userBudget.setCurrentExecutedBudget(executed);

			}

		} catch (SQLException e) {

		}
		return userBudget;
	}

	public float getDailyBudget() {

		float dailyBudget = 0f;
		try {

			List<String> incomeCategoriesIds = new CategoryService(this.context)
					.getFixedExpensesCategoriesIds();

			float monthlyIncome = calculateMonthlyIncome();
			Log.d("BUDGET", "monthlyIncome:" + monthlyIncome);

			float fixedExpenses = getFixedCategoriesBudget(incomeCategoriesIds);
			Log.d("BUDGET", "fixedExpenses:" + fixedExpenses);

			float expensesDueDate = transactionService
					.getTotalTransactionsDueDate();
			Log.d("BUDGET", "expensesDueDate:" + expensesDueDate);

			float todayExpenses = transactionService
					.getTotalTransactionsToday();
			Log.d("BUDGET", "todayDueDate:" + todayExpenses);

			dailyBudget = monthlyIncome - (fixedExpenses - expensesDueDate);
			Log.d("BUDGET", "freeBudget:" + dailyBudget);

			int monthDays = Calendar.getInstance().getActualMaximum(
					Calendar.DAY_OF_MONTH);
			int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			int days = monthDays - currentDay;
			Log.d("BUDGET", "days:" + days);

			dailyBudget = dailyBudget / days;
			dailyBudget -= todayExpenses;
			Log.d("BUDGET", "dailyBudget:" + dailyBudget);

		} catch (Exception e) {
			Log.d("ERROR", e.getMessage());
		}
		return dailyBudget;
	}

	private float getFixedCategoriesBudget(List<String> incomeCategoriesIds) {
		float fixedExpenses = 0f;
		try {

			Dao<UserBudget, String> budgetDao = this.getHelper().getBudgets();

			QueryBuilder<UserBudget, String> budgetQBuilder = budgetDao
					.queryBuilder();

			budgetQBuilder.where().in("idCategory", incomeCategoriesIds);

			PreparedQuery<UserBudget> budgetQuery = budgetQBuilder.prepare();

			List<UserBudget> budgets = budgetDao.query(budgetQuery);

			for (UserBudget expenseBudget : budgets) {
				fixedExpenses += Math.max(expenseBudget.getBudget(),
						transactionService
								.getTopTransactionInCurrentMonth(expenseBudget
										.getIdCategory()));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return fixedExpenses;
	}

	public float calculateMonthlyIncome() {
		float monthlyIncome = 0f;
		try {

			Dao<UserBudget, String> budgetDao = this.getHelper().getBudgets();

			List<String> incomeCategoriesIds = new CategoryService(this.context)
					.getIncomeCategoriesId();

			QueryBuilder<UserBudget, String> budgetQBuilder = budgetDao
					.queryBuilder();

			budgetQBuilder.where().in("idCategory", incomeCategoriesIds);

			PreparedQuery<UserBudget> budgetQuery = budgetQBuilder.prepare();

			List<UserBudget> budgets = budgetDao.query(budgetQuery);

			for (UserBudget incomeBudget : budgets) {
				monthlyIncome += incomeBudget.getBudget();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return monthlyIncome;
	}

}
