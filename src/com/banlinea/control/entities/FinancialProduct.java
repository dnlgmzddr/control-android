package com.banlinea.control.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class FinancialProduct {
	
	public static final int CATEGORY_CASH = 0;
	public static final int CATEGORY_CREDIT_CARD = 1;
	public static final int CATEGORY_SAVING_ACCOUNT = 2;
	
	
	@DatabaseField
	private String excerpt;
	
	@DatabaseField
	private String financialEntityName;

	@DatabaseField(id = true)
	private String id;
	
	@DatabaseField
	private String idFinancialEntity;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private int category;

	public String getExcerpt() {
		return excerpt;
	}

	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	public String getFinancialEntityName() {
		return financialEntityName;
	}

	public void setFinancialEntityName(String financialEntityName) {
		this.financialEntityName = financialEntityName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdFinancialEntity() {
		return idFinancialEntity;
	}

	public void setIdFinancialEntity(String idFinancialEntity) {
		this.idFinancialEntity = idFinancialEntity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
}
