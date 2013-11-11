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
	private java.util.Date toDate;

	@DatabaseField(dataType = DataType.DATE_LONG)
	private java.util.Date fromDate;

	@DatabaseField
	private String link;

	@DatabaseField
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

	public java.util.Date getToDate() {
		return toDate;
	}

	public void setToDate(java.util.Date toDate) {
		this.toDate = toDate;
	}

	public java.util.Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(java.util.Date fromDate) {
		this.fromDate = fromDate;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
