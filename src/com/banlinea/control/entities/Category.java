package com.banlinea.control.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Categories")
public class Category {

	
	public static int GROUP_EXPENSE = 0;
	public static int GROUP_INCOME = 1;
	public static int GROUP_SAVING = 2;

	
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
}
