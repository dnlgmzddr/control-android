package com.banlinea.control.bussiness;

import android.content.Context;

import com.banlinea.control.entities.Category;
import com.banlinea.control.entities.Transaction;
import com.banlinea.control.entities.UserFinancialProduct;
import com.banlinea.control.entities.UserProfile;
import com.banlinea.control.entities.result.TransactionResult;
import com.banlinea.control.remote.RemoteTransactionService;
import com.banlinea.control.remote.util.CallResult;

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
			
			UserFinancialProduct financialProduct = null;
			if(pruductId.equals(UserFinancialProduct.DEFAULT_PRODUCT)){
				
			}
			
			if (currentUser == null || currentCategory == null) {
				return new CallResult(false, "No se encontro un usuario");
			}
			Transaction transaction = new Transaction();
			transaction.setAmount(amount);
			transaction.setComment("");
			transaction.setIdUser(currentUser.getId());
			transaction.setIdCategory(currentCategory.getId());
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
}
