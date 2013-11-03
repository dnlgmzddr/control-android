package com.banlinea.control.bussiness;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.Context;

import com.banlinea.control.dto.in.LoginDTO;
import com.banlinea.control.entities.Category;
import com.banlinea.control.entities.Transaction;
import com.banlinea.control.entities.UserBudget;
import com.banlinea.control.entities.UserFinancialProduct;
import com.banlinea.control.entities.UserProfile;
import com.banlinea.control.entities.result.LoginResult;
import com.banlinea.control.entities.result.UserResult;
import com.banlinea.control.local.DatabaseHelper;
import com.banlinea.control.remote.RemoteAuthenticationService;
import com.banlinea.control.remote.util.CallResult;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;

public class AuthenticationService extends BaseService {

	private RemoteAuthenticationService remoteAuthSerice;

	public AuthenticationService(Context context) {

		super(context);
		remoteAuthSerice = new RemoteAuthenticationService();
	}

	public CallResult Register(UserProfile userProfile) throws SQLException {
		UserResult result = remoteAuthSerice.Register(userProfile);
		if (result != null && result.isSuccessfullOperation()) {

			final DatabaseHelper helper = this.getHelper();
			final UserProfile profileToSave = result.getBody();

			TransactionManager.callInTransaction(helper.getConnectionSource(),
					new Callable<Void>() {
						@Override
						public Void call() throws Exception {
							Dao<UserProfile, String> userProfileDao = helper
									.getUserProfiles();
							Dao<UserFinancialProduct, String> financialProductsDao = helper
									.getUserFinantialProducts();

							for (UserFinancialProduct product : profileToSave
									.getUserFinancialProducts()) {
								financialProductsDao.create(product);
							}
							userProfileDao.create(profileToSave);
							// you could pass back an object here
							return null;
						}
					});

		}
		return result;

	}

	public CallResult Login(String userMail, String password)
			throws SQLException {

		LoginResult result = remoteAuthSerice.Auth(userMail, password);

		if (result != null && result.isSuccessfullOperation()) {

			DatabaseHelper helper = this.getHelper();

			Dao<UserProfile, String> userDao = helper.getUserProfiles();
			Dao<Transaction, String> transactionDao = helper.getTransactions();
			Dao<Category, String> categoriesDao = helper.getCategories();
			Dao<UserFinancialProduct, String> productsDao = helper
					.getUserFinantialProducts();
			Dao<UserBudget, String> budgetDao = helper.getBudgets();

			LoginDTO loginData = result.getBody();
			userDao.create(loginData.getUser());
			if (loginData.getCategories() != null) {
				for (Category cat : loginData.getCategories()) {
					categoriesDao.createOrUpdate(cat);
				}
			}
			if (loginData.getFinancialProducts() != null) {
				for (UserFinancialProduct product : loginData
						.getFinancialProducts()) {
					productsDao.createOrUpdate(product);
				}
			}
			if (loginData.getTransactions() != null) {
				for (Transaction transaction : loginData.getTransactions()) {
					transactionDao.createOrUpdate(transaction);
				}
			}
			if (loginData.getBudgets() != null) {
				for (UserBudget budget : loginData.getBudgets()) {
					budgetDao.createOrUpdate(budget);
				}
			}
		}

		return result;
	}

	public void Logout() throws SQLException {
		DatabaseHelper helper = this.getHelper();
		Dao<UserProfile, String> dao = helper.getUserProfiles();
		dao.delete(dao.queryForAll());
	}

	public boolean isLoggedIn() throws SQLException {
		DatabaseHelper helper = this.getHelper();
		Dao<UserProfile, String> dao = helper.getUserProfiles();
		return dao.countOf() > 0;
	}

	public UserProfile GetUser() {
		try {
			DatabaseHelper helper = this.getHelper();
			Dao<UserProfile, String> dao = helper.getUserProfiles();
			List<UserProfile> users = dao.queryForAll();
			if (users.size() == 0) {
				return null;
			} else {
				return users.get(0);
			}
		} catch (SQLException e) {
			return null;
		}
	}
}
