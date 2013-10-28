package com.banlinea.control.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class UserFinancialProduct extends BaseEntity {

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
