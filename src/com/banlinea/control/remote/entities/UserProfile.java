package com.banlinea.control.remote.entities;

public  class UserProfile extends BaseEntity
{
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

	private String Id;
    private String Name;
    private String Password; 
    private String Mail; 
}
