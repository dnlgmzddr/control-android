package com.banlinea.control.dto.out;

public class FinancialEntitiesRequest {
	private int category;

	public FinancialEntitiesRequest() {

	}

	public FinancialEntitiesRequest(int category) {
		this.category = category;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

}
