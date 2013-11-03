package com.banlinea.control.bussiness;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.banlinea.control.entities.Category;
import com.banlinea.control.entities.Transaction;
import com.banlinea.control.entities.UserFinancialProduct;
import com.banlinea.control.entities.UserProfile;
import com.banlinea.control.entities.result.TransactionResult;
import com.banlinea.control.remote.RemoteTransactionService;
import com.banlinea.control.remote.util.CallResult;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class TransactionService extends BaseService {

	private RemoteTransactionService remoteTransactionService;

	public TransactionService(Context context) {

		super(context);
		remoteTransactionService = new RemoteTransactionService();
	}

	public CallResult AddTransaction(String categoryId, float amount,
			String pruductId) {

		TransactionResult result;

		try {
			UserProfile currentUser = new AuthenticationService(this.context)
					.GetUser();
			Category currentCategory = new CategoryService(this.context)
					.GetCategory(categoryId);

			UserFinancialProduct financialProduct = new FinancialProductService(
					this.context).getProductById(pruductId);

			if (currentUser == null || currentCategory == null
					|| financialProduct == null) {
				return new CallResult(false,
						"No es posible realizar la operación.");
			}
			Transaction transaction = new Transaction();
			transaction.setAmount(amount);
			transaction.setComment("");
			transaction.setIdUser(currentUser.getId());
			transaction.setIdCategory(currentCategory.getId());
			transaction.setIdProduct(financialProduct.getIdProduct());
			result = remoteTransactionService.AddTransaction(transaction);

			if (result.isSuccessfullOperation()) {
				Transaction newTransaction = result.getBody();
				this.getHelper().getTransactions().create(newTransaction);
			}

		} catch (Exception e) {
			result = new TransactionResult();
			result.setMessage("Lo sentimos un error ha ocurrido.");
			result.setSuccessfullOperation(false);
		}

		return result;
	}

	public List<Transaction> getCurrentMonthTransactions() throws SQLException {
		Calendar begin = Calendar.getInstance();
		Calendar end = Calendar.getInstance();

		begin.set(Calendar.DAY_OF_MONTH, 1);
		end.set(Calendar.MONTH, end.get(Calendar.MONDAY) + 1);
		end.set(Calendar.DAY_OF_MONTH, 1);

		Dao<Transaction, String> transactionDao = this.getHelper()
				.getTransactions();

		QueryBuilder<Transaction, String> query = transactionDao.queryBuilder();

		query.where().between("date", begin.getTime(), end.getTime());
		PreparedQuery<Transaction> preparedQuery = query.prepare();
		Log.d("ORMLITE", preparedQuery.toString());
		List<Transaction> transactionInMonth = transactionDao
				.query(preparedQuery);

		return transactionInMonth;
	}

	public List<Transaction> getCurrentMonthTransactionsFor(String idCategory) {
		try {
			Dao<Transaction, String> transactionDAO = this.getHelper()
					.getTransactions();
			List<Transaction> transactions = transactionDAO.queryForEq(
					"idCategory", idCategory);
			return transactions;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public float getTopTransactionInCurrentMonth(String idCategory) {
		double topTransaction = 0f;
		try {

			Calendar begin = Calendar.getInstance();
			Calendar end = Calendar.getInstance();

			begin.set(Calendar.DAY_OF_MONTH, 1);
			end.set(Calendar.MONTH, end.get(Calendar.MONDAY) + 1);
			end.set(Calendar.DAY_OF_MONTH, 1);

			Dao<Transaction, String> transactionDao;

			transactionDao = this.getHelper().getTransactions();

			QueryBuilder<Transaction, String> query = transactionDao
					.queryBuilder();

			Where<Transaction, String> where = query.where();

			where.between("date", begin.getTime(), end.getTime());
			where.and();
			where.eq("idCategory", idCategory);
			query.orderBy("amount", true);

			Log.d("ORMLITE", query.prepareStatementString());

			Transaction tr = transactionDao.queryForFirst(query.prepare());
			if (tr != null) {
				topTransaction = tr.getAmount();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (float) topTransaction;
	}

	public float getExpensesDueDate() {
		float totalExpenses = 0f;
		try {

			List<String> unFixedExpensesCategories = new CategoryService(
					this.context).getUnFixedExpensesCategoriesIds();

			Calendar begin = Calendar.getInstance();
			Calendar end = Calendar.getInstance();
			end.add(Calendar.DAY_OF_MONTH, -1);

			begin.set(Calendar.DAY_OF_MONTH, 1);

			Dao<Transaction, String> transactionDao;

			transactionDao = this.getHelper().getTransactions();

			QueryBuilder<Transaction, String> query = transactionDao
					.queryBuilder();

			Where<Transaction, String> where = query.where();

			where.between("date", begin.getTime(), end.getTime());
			where.and();
			where.in("idCategory", unFixedExpensesCategories);

			Log.d("ORMLITE", query.prepareStatementString());

			List<Transaction> transactions = transactionDao.query(query
					.prepare());

			for (Transaction transaction : transactions) {
				totalExpenses += transaction.getAmount();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return totalExpenses;
	}

}
