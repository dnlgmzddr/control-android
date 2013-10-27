package com.banlinea.control.entities.util;

import com.banlinea.control.entities.Transaction;
import com.banlinea.control.remote.util.CallResult;

public class TransactionResult extends CallResult {

	
	public Transaction getBody() {
		return Body;
	}

	public void setBody(Transaction body) {
		Body = body;
	}

	private Transaction Body;
	
	

}
