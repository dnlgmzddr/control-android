package com.banlinea.control.entities;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Category extends BaseEntity implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2272440612325874578L;
	
	public static final int GROUP_EXPENSE = 0;
	public static final int GROUP_INCOME = 1;
	public static final int GROUP_SAVING = 2;

	public static String SYSTEM_OWNER_ID = "bd487de8-3614-11e3-98fd-ce3f5508acd9";
	public static String SYSTEM_EMPTY_ID = "00000000-0000-0000-0000-000000000000";
	
	@DatabaseField(id=true)
	private String id;
	
	@DatabaseField
	private String idParent;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private String idOwner;
	
	@DatabaseField
	private int group;
	
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdParent() {
		return idParent;
	}
	public void setIdParent(String idParent) {
		this.idParent = idParent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdOwner() {
		return idOwner;
	}
	public void setIdOwner(String idOwner) {
		this.idOwner = idOwner;
	}
	public int getGroup() {
		return group;
	}
	public void setGroup(int group) {
		this.group = group;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

}
