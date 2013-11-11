package com.banlinea.control.entities;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class UserBudget implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3638660564955995096L;

	@DatabaseField(id = true)
	private String idCategory;
	@DatabaseField(index = true)
	private String idUser;
	@DatabaseField
	private float budget;
	@DatabaseField
	private boolean isFixed;

	/**
	 * Indicate if the period of the budget is Monthly, Weekly... etc.
	 */
	@DatabaseField
	private int period;

	private float currentExecutedBudget;

	public String getIdCategory() {
		return idCategory;
	}

	public void setIdCategory(String idCategory) {
		this.idCategory = idCategory;
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public float getBudget() {
		return budget;
	}

	public void setBudget(float budget) {
		this.budget = budget;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public float getCurrentExecutedBudget() {
		return currentExecutedBudget;
	}

	public void setCurrentExecutedBudget(float currentExecutedBudget) {
		this.currentExecutedBudget = currentExecutedBudget;
	}

	public boolean isFixed() {
		return isFixed;
	}

	public void setFixed(boolean isFixed) {
		this.isFixed = isFixed;
	}

}
