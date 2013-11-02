package com.banlinea.control.entities.result;

import com.banlinea.control.entities.Transaction;
import com.banlinea.control.remote.util.CallResult;

public class TransactionResult extends CallResult {

	
	public Transaction getBody() {
		return body;
	}

	public void setBody(Transaction body) {
		this.body = body;
	}

	private Transaction body;
	
	

}
