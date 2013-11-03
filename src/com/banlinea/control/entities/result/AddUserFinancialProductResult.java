package com.banlinea.control.entities.result;

import com.banlinea.control.entities.FinancialProduct;
import com.banlinea.control.remote.util.CallResult;

public class AddUserFinancialProductResult extends CallResult{

	private FinancialProduct body;

	public FinancialProduct getBody() {
		return body;
	}

	public void setBody(FinancialProduct body) {
		this.body = body;
	}
	
}
