package com.banlinea.control.remote;

import java.util.concurrent.ExecutionException;

import com.banlinea.control.entities.Transaction;
import com.banlinea.control.entities.result.TransactionResult;
import com.banlinea.control.remote.util.ApiMethod;
import com.banlinea.control.remote.util.ControlApiHandler;

public class RemoteTransactionService {

	public TransactionResult AddTransaction(Transaction transaction) {
		TransactionResult result = null;

		try {
			result = new ControlApiHandler<TransactionResult, Transaction>(
					transaction,
					ApiMethod.TRNSACTION_ADD,
					TransactionResult.class).execute().get();
			

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

}
