package com.banlinea.control.bussiness;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.banlinea.control.dto.in.FinancialEntityDTO;
import com.banlinea.control.dto.out.FinancialEntitiesRequest;
import com.banlinea.control.dto.out.ProductFilterRequest;
import com.banlinea.control.entities.FinancialProduct;
import com.banlinea.control.entities.UserFinancialProduct;
import com.banlinea.control.entities.result.FinancialEntitiesResult;
import com.banlinea.control.entities.result.FinancialProductResult;
import com.banlinea.control.remote.RemoteFinancialProductService;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

public class FinancialProductService extends BaseService {

	private RemoteFinancialProductService financialProductService;

	public FinancialProductService(Context context) {
		super(context);
		financialProductService = new RemoteFinancialProductService();
	}

	/**
	 * 
	 * @param productId
	 * @return
	 */
	public UserFinancialProduct getProductById(String productId) {
		UserFinancialProduct result = null;
		try {
			Dao<UserFinancialProduct, String> productDao = this.getHelper()
					.getUserFinantialProducts();

			if (productId.equals(UserFinancialProduct.DEFAULT_PRODUCT)) {

				QueryBuilder<UserFinancialProduct, String> queryBuilder = productDao
						.queryBuilder();

				queryBuilder.where().eq("type", UserFinancialProduct.TYPE_CASH);
				PreparedQuery<UserFinancialProduct> preparedQuery = queryBuilder
						.prepare();

				result = productDao.queryForFirst(preparedQuery);
			} else {
				result = productDao.queryForId(productId);
			}
		} catch (SQLException e) {
			result = null;
		}
		return result;
	}

	/**
	 * Get the financial entities that have products in the category specified.
	 * @param category
	 * @return the entities
	 * @throws Exception
	 */
	public List<FinancialEntityDTO> getFinancialEntitiesByType(int category)
			throws Exception {
		List<FinancialEntityDTO> entities;

		FinancialEntitiesResult result = financialProductService
				.getFinancialEntitiesByType(new FinancialEntitiesRequest(
						category));

		if (!result.isSuccessfullOperation()) {	
			throw new Exception(result.getMessage());
		}
		entities = result.getBody();
		return entities;	
	}
	
	
	public List<FinancialProduct> getFilteredProducts(int category, String entityId) throws Exception{
		
		ProductFilterRequest filter = new ProductFilterRequest();
		
		filter.setCategory(category);
		filter.setFinancialEntityId(entityId);
		
		FinancialProductResult result = financialProductService.GetFiltered(filter);
		if(!result.isSuccessfullOperation()){
			throw new Exception(result.getMessage());
		}
		
		return result.getBody();
	}
	
	

}
