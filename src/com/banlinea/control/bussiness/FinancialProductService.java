package com.banlinea.control.bussiness;

import java.sql.SQLException;

import android.content.Context;

import com.banlinea.control.entities.UserFinancialProduct;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

public class FinancialProductService extends BaseService {

	public FinancialProductService(Context context) {

		super(context);
	}

	public UserFinancialProduct getProductById(String productId) {
		UserFinancialProduct result = null;
		try {
			Dao<UserFinancialProduct, String> productDao = this.getHelper()
					.getUserFinantialProducts();

			if (productId.equals(UserFinancialProduct.DEFAULT_PRODUCT)) {

				QueryBuilder<UserFinancialProduct, String> queryBuilder = productDao.queryBuilder();

				queryBuilder.where().eq("type", UserFinancialProduct.TYPE_CASH);
				PreparedQuery<UserFinancialProduct> preparedQuery = queryBuilder.prepare();
				
				result = productDao.queryForFirst(preparedQuery);
			} else {
				result = productDao.queryForId(productId);
			}
		} catch (SQLException e) {
			result = null;
		}
		return result;
	}

}
