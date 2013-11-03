package com.banlinea.control.entities;

import java.io.Serializable;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable
public  class UserProfile  implements Serializable
{
	
	
	@DatabaseField(id=true)
	private String id;
	@DatabaseField
    private String name;
	@DatabaseField
    private String password; 
	@DatabaseField
    private String mail; 
	private List<UserFinancialProduct> userFinancialProducts;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4407453165072554782L;
	
	public UserProfile()
    {
    }

    public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public List<UserFinancialProduct> getUserFinancialProducts() {
		return userFinancialProducts;
	}

	public void setUserFinancialProducts(List<UserFinancialProduct> userFinancialProducts) {
		this.userFinancialProducts = userFinancialProducts;
	}

	
}
