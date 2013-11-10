package com.banlinea.control.entities;

import java.io.Serializable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Transaction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4603073635487240094L;
	
	public static final int TYPE_EXPENSE = Category.GROUP_EXPENSE;
	public static final int TYPE_INCOME = Category.GROUP_INCOME;
	public static final int TYPE_SAVING = Category.GROUP_SAVING;
	
	
	@DatabaseField(id = true)
	private String id;

	@DatabaseField
	private String idProduct;
	
	@DatabaseField
	private double amount;

	@DatabaseField(index = true)
	private String idCategory;

	@DatabaseField
	private String comment;

	@DatabaseField(dataType = DataType.DATE_LONG)
	private java.util.Date date = new java.util.Date();	

	@DatabaseField
	private String idUser;

	@DatabaseField
	private int periodType;

	@DatabaseField
	private int type;

	
	
	public final double getAmount() {
		return amount;
	}

	public final void setAmount(double value) {
		amount = value;
	}
	
	
	public final int getType() {
		return type;
	}

	public final void setType(int value) {
		type = value;
	}
	
	public final int getPeriodType() {
		return periodType;
	}

	public final void setPeriodType(int value) {
		periodType = value;
	}
	

	public final String getIdUser() {
		return idUser;
	}

	public final void setIdUser(String value) {
		idUser = value;
	}
	
	public final String getIdCategory() {
		return idCategory;
	}

	public final void setIdCategory(String value) {
		idCategory = value;
	}
	
	public final String getIdProduct() {
		return idProduct;
	}

	public final void setIdProduct(String value) {
		idProduct = value;
	}
	
	public final java.util.Date getDate() {
		return date;
	}

	public final void setDate(java.util.Date value) {
		date = value;
	}
	
	public final String getComment() {
		return comment;
	}

	public final void setComment(String value) {
		comment = value;
	}
	

	public final String getId() {
		return id;
	}

	public final void setId(String value) {
		id = value;
	}

}
