package com.banlinea.control.bussiness;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.banlinea.control.dto.in.FinancialEntityDTO;
import com.banlinea.control.dto.out.FinancialEntitiesRequest;
import com.banlinea.control.dto.out.ProductFilterRequest;
import com.banlinea.control.entities.FinancialProduct;
import com.banlinea.control.entities.UserFinancialProduct;
import com.banlinea.control.entities.UserProfile;
import com.banlinea.control.entities.result.AddUserFinancialProductResult;
import com.banlinea.control.entities.result.FinancialEntitiesResult;
import com.banlinea.control.entities.result.FinancialProductResult;
import com.banlinea.control.remote.RemoteFinancialProductService;
import com.banlinea.control.remote.util.CallResult;
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
	 * Get user products with full financial products.
	 * 
	 * @return
	 */
	public List<UserFinancialProduct> getUserProducts() {

		List<UserFinancialProduct> userProducts = null;

		try {
			UserProfile userProfile = new AuthenticationService(this.context)
					.GetUser();
			Dao<UserFinancialProduct, String> userProductsDao = this
					.getHelper().getUserFinantialProducts();

			userProducts = userProductsDao.queryForEq("idUser",
					userProfile.getId());

			for (UserFinancialProduct uProduct : userProducts) {
				uProduct.setProduct(this.getFinancialProductById(uProduct
						.getIdProduct()));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return userProducts;
	}

	/**
	 * Get financial product by Id.
	 * 
	 * @param productId
	 * @return
	 */
	public FinancialProduct getFinancialProductById(String productId) {
		FinancialProduct product = null;
		try {
			Dao<FinancialProduct, String> productsDao = this.getHelper()
					.getFinantialProducts();
			product = productsDao.queryForId(productId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return product;
	}

	/**
	 * Get user financial products. (without dependent financial product)
	 * 
	 * @param productId
	 * @return
	 */
	public UserFinancialProduct getUserProductById(String productId) {
		UserFinancialProduct result = null;
		try {
			Dao<UserFinancialProduct, String> productDao = this.getHelper()
					.getUserFinantialProducts();

			if (productId.equals(UserFinancialProduct.DEFAULT_PRODUCT)) {

				QueryBuilder<UserFinancialProduct, String> queryBuilder = productDao
						.queryBuilder();

				queryBuilder.where().eq("category", FinancialProduct.CATEGORY_CASH);
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
	 * 
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

	public List<FinancialProduct> getFilteredProducts(int category,
			String entityId) throws Exception {

		ProductFilterRequest filter = new ProductFilterRequest();

		filter.setCategory(category);
		filter.setFinancialEntityId(entityId);

		FinancialProductResult result = financialProductService
				.GetFiltered(filter);
		if (!result.isSuccessfullOperation()) {
			throw new Exception(result.getMessage());
		}

		return result.getBody();
	}

	/**
	 * Add a financial product to the user.
	 * 
	 * @param name
	 * @param productId
	 * @param productCategory
	 * @return
	 * @throws Exception 
	 */	
	public CallResult AddProduct(String name, String productId, int productCategory) throws Exception{
		
		UserProfile user = new AuthenticationService(this.context).GetUser();
		
		UserFinancialProduct userProduct = new UserFinancialProduct();
		
		userProduct.setIdProduct(productId);
		userProduct.setName(name);
		userProduct.setCategory(productCategory);
		userProduct.setIdUser(user.getId());
		
		AddUserFinancialProductResult result = this.financialProductService.AddProductToUser(userProduct);
		if(!result.isSuccessfullOperation()){
			throw new Exception(result.getMessage());
		}
		
		this.getHelper().getUserFinantialProducts().createOrUpdate(userProduct);
		this.getHelper().getFinantialProducts().createOrUpdate(result.getBody());
		
		return result;
	}

}
