package com.banlinea.control.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class UserFinancialProduct {

	public static final String DEFAULT_PRODUCT = "DEFAULT";
	public static final int TYPE_CASH = 0;
	public static final int TYPE_CREDIT_CARD = 0;
	public static final int TYPE_SAVING_ACCOUNT = 0;
	
	@DatabaseField(id = true)
	private String idProduct;


	@DatabaseField
	private String idUser;
	@DatabaseField
	private String name;
	@DatabaseField
	private int type;
	
	public String getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(String idProduct) {
		this.idProduct = idProduct;
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
