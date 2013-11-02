package com.banlinea.control.bussiness;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.banlinea.control.entities.Category;
import com.banlinea.control.entities.Transaction;
import com.banlinea.control.entities.UserBudget;
import com.banlinea.control.entities.UserFinancialProduct;
import com.banlinea.control.entities.UserProfile;
import com.banlinea.control.entities.result.TransactionResult;
import com.banlinea.control.remote.RemoteTransactionService;
import com.banlinea.control.remote.util.CallResult;
import com.j256.ormlite.dao.Dao;

public class TransactionService extends BaseService {

	private RemoteTransactionService remoteTransactionService;

	public TransactionService(Context context) {

		super(context);
		remoteTransactionService = new RemoteTransactionService();
	}

	public CallResult AddTransaction(String categoryId, float amount, String pruductId) {

		TransactionResult result;

		try {
			UserProfile currentUser = new AuthenticationService(this.context)
					.GetUser();
			Category currentCategory = new CategoryService(this.context)
					.GetCategory(categoryId);
			
			UserFinancialProduct financialProduct = new FinancialProductService(this.context).getProductById(pruductId);
			
			
			if (currentUser == null || currentCategory == null || financialProduct == null) {
				return new CallResult(false, "No es posible realizar la operación.");
			}
			Transaction transaction = new Transaction();
			transaction.setAmount(amount);
			transaction.setComment("");
			transaction.setIdUser(currentUser.getId());
			transaction.setIdCategory(currentCategory.getId());
			transaction.setIdProduct(financialProduct.getIdProduct());
			result = remoteTransactionService.AddTransaction(transaction);
			
			if(result.isSuccessfullOperation()){
				Transaction  newTransaction = result.getBody();
				this.getHelper().getTransactions().create(newTransaction);	
			}

		} catch (Exception e) {
			result  = new TransactionResult();
			result.setMessage("Lo sentimos un error ha ocurrido.");
			result.setSuccessfullOperation(false);
		} 

		return result;
	}
	
	public List<Transaction> getCurrentMonthTransactions(){
		
		Date today = new Date();
		
		return null;
	}

	public List<Transaction> getCurrentMonthTransactionsFor(String idCategory) {
		try {
			// TODO: set for time period
			Dao<Transaction,String> transactionDAO = this.getHelper().getTransactions();
			List<Transaction> transactions = transactionDAO.queryForEq("idCategory", idCategory);
			return transactions;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
