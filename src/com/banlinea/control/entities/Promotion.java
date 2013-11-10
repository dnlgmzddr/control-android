package com.banlinea.control.entities;

import java.util.List;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Promotion {
	@DatabaseField
	private String id;
	@DatabaseField
	private String title;
	@DatabaseField
	private String excerpt;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private java.util.Date ultilDate;
	
	@DatabaseField
	private String link;
	
	private String csvFinancialProducts;
	
	private List<UserFinancialProduct> relatedProducts;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getExcerpt() {
		return excerpt;
	}

	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	public java.util.Date getUltilDate() {
		return ultilDate;
	}

	public void setUltilDate(java.util.Date ultilDate) {
		this.ultilDate = ultilDate;
	}

	public String getCsvFinancialProducts() {
		return csvFinancialProducts;
	}

	public void setCsvFinancialProducts(String csvFinancialProducts) {
		this.csvFinancialProducts = csvFinancialProducts;
	}

	public List<UserFinancialProduct> getRelatedProducts() {
		return relatedProducts;
	}

	public void setRelatedProducts(List<UserFinancialProduct> relatedProducts) {
		this.relatedProducts = relatedProducts;
	}
	
	
	
	
}
