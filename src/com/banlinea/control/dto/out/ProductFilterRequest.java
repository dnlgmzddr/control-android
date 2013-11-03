package com.banlinea.control.dto.out;

public class ProductFilterRequest {
	
	private int category;
	private String financialEntityId;

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getFinancialEntityId() {
		return financialEntityId;
	}

	public void setFinancialEntityId(String financialEntityId) {
		this.financialEntityId = financialEntityId;
	}

}
