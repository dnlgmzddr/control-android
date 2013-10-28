package com.banlinea.control.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class UserFinancialProduct extends BaseEntity {

	public static final String DEFAULT_PRODUCT = "DEFAULT";
	public static final int TYPE_CASH = 0;
	public static final int TYPE_CREDIT_CARD = 0;
	public static final int TYPE_SAVING_ACCOUNT = 0;
	
	@DatabaseField
	private String IdProduct;

	public String getIdProduct() {
		return IdProduct;
	}

	public void setIdProduct(String idProduct) {
		IdProduct = idProduct;
	}

	public String getIdUser() {
		return IdUser;
	}

	public void setIdUser(String idUser) {
		IdUser = idUser;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}

	@DatabaseField
	private String IdUser;
	@DatabaseField
	private String Name;
	@DatabaseField
	private int Type;
}
