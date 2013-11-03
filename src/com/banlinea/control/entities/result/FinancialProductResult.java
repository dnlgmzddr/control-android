package com.banlinea.control.entities.result;

import java.util.List;

import com.banlinea.control.entities.FinancialProduct;
import com.banlinea.control.remote.util.CallResult;

public class FinancialProductResult extends CallResult {

	private List<FinancialProduct> body;

	public List<FinancialProduct> getBody() {
		return body;
	}

	public void setBody(List<FinancialProduct> body) {
		this.body = body;
	}
	
	
	
}
