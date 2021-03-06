package com.banlinea.control.dto.in;

import java.util.List;

import com.banlinea.control.entities.Category;
import com.banlinea.control.entities.FinancialProduct;
import com.banlinea.control.entities.Transaction;
import com.banlinea.control.entities.UserBudget;
import com.banlinea.control.entities.UserFinancialProduct;
import com.banlinea.control.entities.UserProfile;
import com.banlinea.control.remote.util.CallResult;


public class LoginDTO extends CallResult {

	
	
	
	private UserProfile user;
	private List<Category> categories;
	private List<UserFinancialProduct> userProducts;
	private List<UserBudget> budgets;
	private List<FinancialProduct> financialProducts;
	
	private List<Transaction> transactions;

	public UserProfile getUser() {
		return user;
	}

	public void setUser(UserProfile user) {
		this.user = user;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<FinancialProduct> getFinancialProducts() {
		return financialProducts;
	}

	public void setFinancialProducts(List<FinancialProduct> financialProducts) {
		this.financialProducts = financialProducts;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public List<UserBudget> getBudgets() {
		return budgets;
	}

	public void setBudgets(List<UserBudget> budgets) {
		this.budgets = budgets;
	}

	public List<UserFinancialProduct> getUserProducts() {
		return userProducts;
	}

	public void setUserProducts(List<UserFinancialProduct> userProducts) {
		this.userProducts = userProducts;
	}
	
	
	
	
}
