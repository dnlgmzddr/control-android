package com.banlinea.control.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class UserBudget extends BaseEntity {
	
	@DatabaseField(id = true)
	private String IdCategory;
	@DatabaseField(index = true)
	private String IdUser;
	@DatabaseField
	private float Budget;
	
	/**
	 * Indicate if the period of the budget is Monthly, Weekly... etc.
	 */
	@DatabaseField
	private int Period;
	
	public String getIdCategory() {
		return IdCategory;
	}
	public void setIdCategory(String idCategory) {
		IdCategory = idCategory;
	}
	public String getIdUser() {
		return IdUser;
	}
	public void setIdUser(String idUser) {
		IdUser = idUser;
	}
	public float getBudget() {
		return Budget;
	}
	public void setBudget(float budget) {
		Budget = budget;
	}
	public int getPeriod() {
		return Period;
	}
	public void setPeriod(int period) {
		Period = period;
	}
	
	
}
