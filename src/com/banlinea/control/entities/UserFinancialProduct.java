package com.banlinea.control.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class UserFinancialProduct {

	public static final String DEFAULT_PRODUCT = "DEFAULT";
	
	
	@DatabaseField(id = true)
	private String idProduct;


	@DatabaseField
	private String idUser;
	@DatabaseField
	private String name;
	
	/**
	 * Category of the product
	 */
	@DatabaseField
	private int category;
	
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

	public int getCategory() {
		return category;
	}

	public void setCategory(int type) {
		this.category = type;
	}

}
