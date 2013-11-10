package com.banlinea.control.local;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.banlinea.control.R;
import com.banlinea.control.entities.Category;
import com.banlinea.control.entities.FinancialProduct;
import com.banlinea.control.entities.Promotion;
import com.banlinea.control.entities.Transaction;
import com.banlinea.control.entities.UserBudget;
import com.banlinea.control.entities.UserFinancialProduct;
import com.banlinea.control.entities.UserProfile;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "bl.control.db";
	private static final int DATABASE_VERSION = 7;

	private Dao<UserProfile, String> userProfiles;
	private Dao<Category, String> categories;
	private Dao<UserBudget, String> budgets;
	private Dao<Transaction, String> transactions;
	private Dao<UserFinancialProduct, String> userFinantialProducts;
	private Dao<Promotion, String> promotions;
	
	private Dao<FinancialProduct, String> finantialProducts;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION,
				R.raw.ormlite_config);
	}

	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase,
			ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, UserProfile.class);
			TableUtils.createTable(connectionSource, Category.class);
			TableUtils.createTable(connectionSource, UserBudget.class);
			TableUtils.createTable(connectionSource, Transaction.class);
			TableUtils.createTable(connectionSource, UserFinancialProduct.class);
			TableUtils.createTable(connectionSource, FinancialProduct.class);
			TableUtils.createTable(connectionSource, Promotion.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase,
			ConnectionSource connectionSource, int oldVer, int newVer) {
		try {
			TableUtils.dropTable(connectionSource, UserProfile.class, true);
			TableUtils.dropTable(connectionSource, Category.class, true);
			TableUtils.dropTable(connectionSource, UserBudget.class, true);
			TableUtils.dropTable(connectionSource, Transaction.class, true);
			TableUtils.dropTable(connectionSource, UserFinancialProduct.class, true);
			TableUtils.dropTable(connectionSource, FinancialProduct.class, true);
			TableUtils.dropTable(connectionSource, Promotion.class, true);
			onCreate(sqliteDatabase, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(),
					"Unable to upgrade database from version " + oldVer
							+ " to new " + newVer, e);
		}
	}

	public Dao<UserProfile, String> getUserProfiles() throws SQLException {
		if (userProfiles == null) {
			userProfiles = getDao(UserProfile.class);
		}
		return userProfiles;
	}

	public Dao<Category, String> getCategories() throws SQLException {
		if (categories == null) {
			categories = getDao(Category.class);
		}
		return categories;
	}

	public Dao<UserBudget, String> getBudgets() throws SQLException {
		if (budgets == null) {
			budgets = getDao(UserBudget.class);
		}
		return budgets;
	}
	
	public Dao<Transaction, String> getTransactions() throws SQLException {
		if (transactions == null) {
			transactions = getDao(Transaction.class);
		}
		return transactions;
	}
	public Dao<UserFinancialProduct, String> getUserFinantialProducts() throws SQLException {
		if (userFinantialProducts == null) {
			userFinantialProducts = getDao(UserFinancialProduct.class);
		}
		return userFinantialProducts;
	}
	
	public Dao<FinancialProduct, String> getFinantialProducts() throws SQLException {
		if (finantialProducts == null) {
			finantialProducts = getDao(FinancialProduct.class);
		}
		return finantialProducts;
	}
	
	public Dao<Promotion, String> getPromotions() throws SQLException {
		if (promotions == null) {
			promotions = getDao(FinancialProduct.class);
		}
		return promotions;
	}

}
