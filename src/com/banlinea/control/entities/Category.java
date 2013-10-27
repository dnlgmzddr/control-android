package com.banlinea.control.entities;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Categories")
public class Category extends BaseEntity implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2272440612325874578L;
	
	public static int GROUP_EXPENSE = 0;
	public static int GROUP_INCOME = 1;
	public static int GROUP_SAVING = 2;

	public static String SYSTEM_OWNER_ID = "bd487de8-3614-11e3-98fd-ce3f5508acd9";
	public static String SYSTEM_EMPTY_ID = "00000000-0000-0000-0000-000000000000";
	
	@DatabaseField(id=true)
	private String Id;
	
	@DatabaseField
	private String IdParent;
	
	@DatabaseField
	private String Name;
	
	@DatabaseField
	private String IdOwner;
	
	@DatabaseField
	private int Group;
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getIdParent() {
		return IdParent;
	}
	public void setIdParent(String idParent) {
		IdParent = idParent;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getIdOwner() {
		return IdOwner;
	}
	public void setIdOwner(String idOwner) {
		IdOwner = idOwner;
	}
	public int getGroup() {
		return Group;
	}
	public void setGroup(int group) {
		Group = group;
	}
	
	@Override
	public String toString() {
		return this.Name;
	}

}
