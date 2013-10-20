package com.banlinea.control.entities;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public  class UserProfile extends BaseEntity implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4407453165072554782L;
	
	public UserProfile()
    {
    }

    public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getMail() {
		return Mail;
	}

	public void setMail(String mail) {
		Mail = mail;
	}

	@DatabaseField(id=true)
	private String Id;
	@DatabaseField
    private String Name;
	@DatabaseField
    private String Password; 
	@DatabaseField
    private String Mail; 
}
