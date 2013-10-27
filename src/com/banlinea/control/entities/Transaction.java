package com.banlinea.control.entities;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Transaction extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4603073635487240094L;
	
	@DatabaseField
	private double amount;

	public final double getAmount() {
		return amount;
	}

	public final void setAmount(double value) {
		amount = value;
	}

	@DatabaseField
	private String comment;

	public final String getComment() {
		return comment;
	}

	public final void setComment(String value) {
		comment = value;
	}

	@DatabaseField
	private java.util.Date date = new java.util.Date(0);

	public final java.util.Date getDate() {
		return date;
	}

	public final void setDate(java.util.Date value) {
		date = value;
	}

	@DatabaseField(id = true)
	private String id;

	public final String getId() {
		return id;
	}

	public final void setId(String value) {
		id = value;
	}

	@DatabaseField
	private String idAccount;

	public final String getIdAccount() {
		return idAccount;
	}

	public final void setIdAccount(String value) {
		idAccount = value;
	}

	@DatabaseField(index = true)
	private String idCategory;

	public final String getIdCategory() {
		return idCategory;
	}

	public final void setIdCategory(String value) {
		idCategory = value;
	}

	@DatabaseField
	private String idUser;

	public final String getIdUser() {
		return idUser;
	}

	public final void setIdUser(String value) {
		idUser = value;
	}

	@DatabaseField
	private int periodType;

	public final int getPeriodType() {
		return periodType;
	}

	public final void setPeriodType(int value) {
		periodType = value;
	}

	@DatabaseField
	private int type;

	public final int getType() {
		return type;
	}

	public final void setType(int value) {
		type = value;
	}
}
