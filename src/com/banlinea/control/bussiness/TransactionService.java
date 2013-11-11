package com.banlinea.control.bussiness;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.banlinea.control.dto.out.FullTransaction;
import com.banlinea.control.entities.Category;
import com.banlinea.control.entities.Transaction;
import com.banlinea.control.entities.UserFinancialProduct;
import com.banlinea.control.entities.UserProfile;
import com.banlinea.control.entities.definitions.SafeSpendPeriod;
import com.banlinea.control.entities.result.TransactionResult;
import com.banlinea.control.remote.RemoteTransactionService;
import com.banlinea.control.remote.util.CallResult;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class TransactionService extends BaseService {

	private RemoteTransactionService remoteTransactionService;

	public TransactionService(Context context) {

		super(context);
		remoteTransactionService = new RemoteTransactionService();
	}

	public CallResult AddTransaction(String categoryId, float amount,
			String productId) {

		TransactionResult result;

		try {
			UserProfile currentUser = new AuthenticationService(this.context)
					.GetUser();
			Category currentCategory = new CategoryService(this.context)
					.GetCategory(categoryId);

			UserFinancialProduct financialProduct = new FinancialProductService(
					this.context).getUserProductById(productId);

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

	/**
	 * Get the transaction for the passed category until now.
	 * 
	 * @param idCategory
	 * @return
	 */
	public List<Transaction> getCurrentMonthTransactionsFor(String idCategory) {
		try {
			Dao<Transaction, String> transactionDAO = this.getHelper()
					.getTransactions();

			Calendar begin = Calendar.getInstance();
			Calendar end = Calendar.getInstance();

			begin.set(Calendar.DAY_OF_MONTH, 1);
			begin.set(Calendar.HOUR_OF_DAY, 0);
			begin.set(Calendar.MINUTE, 0);
			begin.set(Calendar.SECOND, 0);

			QueryBuilder<Transaction, String> query = transactionDAO
					.queryBuilder();

			Where<Transaction, String> where = query.where();

			where.between("date", begin.getTime(), end.getTime());
			where.and();
			where.eq("idCategory", idCategory);

			List<Transaction> transactions = transactionDAO.query(query
					.prepare());

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

	/**
	 * Get the total of transactions due date (exclude income ones) not include
	 * the ones that happen today.
	 * 
	 * @param period
	 * @return total of money register.
	 */
	public float getTotalTransactionsDueDate(SafeSpendPeriod period, List<String> unFixedCategories) {
		float totalExpenses = 0f;
		try {

			
			Calendar begin = Calendar.getInstance();
			Calendar end = Calendar.getInstance();

			begin.set(Calendar.DAY_OF_MONTH, 1);
			begin.set(Calendar.HOUR_OF_DAY, 0);
			begin.set(Calendar.MINUTE, 0);
			begin.set(Calendar.SECOND, 0);

			switch (period) {
			case DAY:
				end.add(Calendar.DAY_OF_MONTH, -1);
				end.set(Calendar.HOUR_OF_DAY, 23);
				end.set(Calendar.MINUTE, 59);
				end.set(Calendar.SECOND, 59);
				break;
			case WEEK:
				end.add(Calendar.DAY_OF_WEEK,
						end.getFirstDayOfWeek() - end.get(Calendar.DAY_OF_WEEK));
				break;
			case MONTH:
				return 0;
			default:
				break;
			}

			Dao<Transaction, String> transactionDao;

			transactionDao = this.getHelper().getTransactions();

			QueryBuilder<Transaction, String> query = transactionDao
					.queryBuilder();

			Where<Transaction, String> where = query.where();

			where.between("date", begin.getTime(), end.getTime());
			where.and();
			where.ne("type", Transaction.TYPE_INCOME);
			where.and();
			where.in("idCategory", unFixedCategories);

			Log.d("ORMLITE", query.prepareStatementString());

			List<Transaction> transactions = transactionDao.query(query
					.prepare());

			for (Transaction transaction : transactions) {
				totalExpenses += transaction.getAmount();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalExpenses;
	}

	public float getTotalTransactionsInPeriod(SafeSpendPeriod period,
			List<String> unFixedCategories) {
		float totalExpenses = 0f;
		try {

			Calendar begin = Calendar.getInstance();
			Calendar end = Calendar.getInstance();

			switch (period) {
			case DAY:

			case WEEK:
				begin.add(Calendar.DAY_OF_WEEK, begin.getFirstDayOfWeek()
						- begin.get(Calendar.DAY_OF_WEEK));
				end = (Calendar) begin.clone();
				end.add(Calendar.DAY_OF_YEAR, 6);
				break;
			case MONTH:
				begin.set(Calendar.DAY_OF_MONTH, 1);
			default:
				break;
			}

			begin.set(Calendar.HOUR_OF_DAY, 0);
			begin.set(Calendar.MINUTE, 0);
			begin.set(Calendar.SECOND, 0);

			end.set(Calendar.HOUR_OF_DAY, 23);
			end.set(Calendar.MINUTE, 59);
			end.set(Calendar.SECOND, 59);

			Dao<Transaction, String> transactionDao;

			transactionDao = this.getHelper().getTransactions();

			QueryBuilder<Transaction, String> query = transactionDao
					.queryBuilder();

			Where<Transaction, String> where = query.where();

			where.between("date", begin.getTime(), end.getTime());
			where.and();
			where.ne("type", Transaction.TYPE_INCOME);
			where.and();
			where.in("idCategory", unFixedCategories);

			Log.d("ORMLITE", query.prepareStatementString());

			List<Transaction> transactions = transactionDao.query(query
					.prepare());

			for (Transaction transaction : transactions) {
				totalExpenses += transaction.getAmount();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalExpenses;
	}

	
	
	public List<FullTransaction> getTransactions(SafeSpendPeriod period){
		
		List<FullTransaction> fullTransactions = new ArrayList<FullTransaction>();
		
		List<Transaction> transactions = this.getTransactionInPeriod(period);
		if(transactions.size() == 0){
			return fullTransactions;
		}
		// user data
		List<UserFinancialProduct> userProducts = new FinancialProductService(this.context).getUserProducts();
		List<Category> categories = new CategoryService(this.context).getAllCategories();
		
		// make base data indexable.
		HashMap<String, UserFinancialProduct> indexableUserProducts = new HashMap<String, UserFinancialProduct>();
		HashMap<String, Category> indexableCategories = new HashMap<String, Category>();
		for (Category category : categories) {
			indexableCategories.put(category.getId(), category);
		}
		for (UserFinancialProduct product : userProducts) {
			indexableUserProducts.put(product.getIdProduct(), product);
		}
		
		for (Transaction transaction : transactions) {
			
			FullTransaction fullTransaction = new FullTransaction();
			
			fullTransaction.setAmount(transaction.getAmount());
			fullTransaction.setComment(transaction.getComment());
			fullTransaction.setDate(transaction.getDate());
			
			fullTransaction.setId(transaction.getId());
			fullTransaction.setIdCategory(transaction.getIdCategory());
			fullTransaction.setIdProduct(transaction.getIdProduct());
			
			fullTransaction.setIdUser(transaction.getIdUser());
			fullTransaction.setPeriodType(transaction.getPeriodType());
			fullTransaction.setType(transaction.getType());
			
			Category baseCategory = indexableCategories.get(transaction.getIdCategory());
			fullTransaction.setCategoryName(baseCategory.getName());
			fullTransaction.setParentCategoryName(indexableCategories.get(baseCategory.getIdParent()).getName());
			
			fullTransaction.setProductName(indexableUserProducts.get(transaction.getIdProduct()).getName());
			fullTransactions.add(fullTransaction);
			
		}
		
		
		return new ArrayList<FullTransaction>();
	}
	
	
	private List<Transaction> getTransactionInPeriod(SafeSpendPeriod period){
		List<Transaction> retTransactions = new ArrayList<Transaction>();
		try {

			Calendar begin = Calendar.getInstance();
			Calendar end = Calendar.getInstance();

			switch (period) {
			case DAY:

			case WEEK:
				begin.add(Calendar.DAY_OF_WEEK, begin.getFirstDayOfWeek()
						- begin.get(Calendar.DAY_OF_WEEK));
				end = (Calendar) begin.clone();
				end.add(Calendar.DAY_OF_YEAR, 6);
				break;
			case MONTH:
				begin.set(Calendar.DAY_OF_MONTH, 1);
			default:
				break;
			}

			begin.set(Calendar.HOUR_OF_DAY, 0);
			begin.set(Calendar.MINUTE, 0);
			begin.set(Calendar.SECOND, 0);

			end.set(Calendar.HOUR_OF_DAY, 23);
			end.set(Calendar.MINUTE, 59);
			end.set(Calendar.SECOND, 59);

			Dao<Transaction, String> transactionDao;

			transactionDao = this.getHelper().getTransactions();

			QueryBuilder<Transaction, String> query = transactionDao
					.queryBuilder();

			Where<Transaction, String> where = query.where();

			where.between("date", begin.getTime(), end.getTime());
			

			Log.d("ORMLITE", query.prepareStatementString());

			retTransactions  = transactionDao.query(query
					.prepare());

			

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retTransactions;
	}
}
