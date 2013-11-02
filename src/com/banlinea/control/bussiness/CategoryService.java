package com.banlinea.control.bussiness;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.banlinea.control.entities.Category;
import com.banlinea.control.entities.UserProfile;
import com.banlinea.control.entities.result.CategoryResult;
import com.banlinea.control.entities.result.GetAllBasicCateoriesResult;
import com.banlinea.control.local.DatabaseHelper;
import com.banlinea.control.remote.RemoteCategoryService;
import com.banlinea.control.remote.util.CallResult;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

public class CategoryService extends BaseService {

	private RemoteCategoryService remoteCategorySerice;

	public CategoryService(Context context) {

		super(context);
		remoteCategorySerice = new RemoteCategoryService();
	}

	/**
	 * Import the defaults user categories.
	 * 
	 * @throws SQLException
	 */
	public void ImportBaseCategories() throws SQLException {
		DatabaseHelper helper = this.getHelper();
		Dao<Category, String> dao = helper.getCategories();
		if (dao.countOf() == 0) {
			GetAllBasicCateoriesResult result = remoteCategorySerice
					.getAllBasics();
			if (result != null && result.isSuccessfullOperation()) {
				for (Category category : result.getBody()) {
					dao.create(category);
				}
			}
		}
	}

	public CallResult AddCustomCategory(String idCategory,String name, int group, String idParent) {
		UserProfile current = new AuthenticationService(this.context).GetUser();
		if (current == null) {
			return new CallResult(false, "No se encontro un usuario");
		}
		Category categoryToAdd = new Category();
		categoryToAdd.setName(name);
		categoryToAdd.setGroup(group);
		categoryToAdd.setIdParent(idParent == null ? Category.SYSTEM_EMPTY_ID
				: idParent);
		
		categoryToAdd.setId(idCategory == null ? Category.SYSTEM_EMPTY_ID
				: idCategory);
		categoryToAdd.setIdOwner(current.getId());

		CategoryResult result = remoteCategorySerice.addCustom(categoryToAdd);
		if (!result.isSuccessfullOperation()) {
			return result;
		}

		try {
			super.getHelper().getCategories().createOrUpdate(result.getBody());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
		}

	public CallResult AddCustomCategory(String name, int group, String idParent) {
		return this.AddCustomCategory(null,name, group, idParent);
	}
	
	/**
	 * Add or update a custom user category.
	 * 
	 * @param name
	 * @param group
	 * @return
	 */
	public CallResult AddCustomCategory(String name, int group) {
		return this.AddCustomCategory(null,name, group, null);
	}

	public CallResult AddCustomCategory(String idCategory, String name, int group) {
		return this.AddCustomCategory(idCategory,name, group, null);
	}
	
	/**
	 * Get category per Group (Expenses, Savings, Incomes)
	 * 
	 * @param group
	 *            Expenses, Savings, Incomes
	 * @return the list of categories.
	 * @throws SQLException
	 */
	public List<Category> GetParentCategoriesPerGroup(int group)
			throws SQLException {
		DatabaseHelper helper = this.getHelper();
		Dao<Category, String> dao = helper.getCategories();

		QueryBuilder<Category, String> queryBuilder = dao.queryBuilder();

		queryBuilder.where().eq("idParent", Category.SYSTEM_EMPTY_ID).and()
				.eq("group", group);

		PreparedQuery<Category> preparedQuery = queryBuilder.prepare();

		List<Category> categories = dao.query(preparedQuery);

		return categories;
	}

	/**
	 * Look up for the child categories of the desired category.
	 * 
	 * @param idParentCategory
	 *            , Id of the parent category.
	 * @return The list of categories that have the parent passed as argument.
	 * @throws SQLException
	 */
	public List<Category> GetChilds(String idParentCategory)
			throws SQLException {
		DatabaseHelper helper = this.getHelper();
		Dao<Category, String> dao = helper.getCategories();

		QueryBuilder<Category, String> queryBuilder = dao.queryBuilder();

		queryBuilder.where().eq("idParent", idParentCategory);

		PreparedQuery<Category> preparedQuery = queryBuilder.prepare();

		List<Category> categories = dao.query(preparedQuery);
		return categories;
	}

	/**
	 * Get a single category by ID
	 * 
	 * @param categoryId
	 *            id of the category to find
	 * @return Category that match, null if nothing is found.
	 * @throws SQLException
	 */
	public Category GetCategory(String categoryId) throws SQLException {
		DatabaseHelper helper = this.getHelper();
		Dao<Category, String> dao = helper.getCategories();
		Category category = dao.queryForId(categoryId);
		return category;
	}
	
	public List<String> getIncomeCategoriesId() throws SQLException {
		Dao<Category, String> catDao = this.getHelper().getCategories();
		List<Category> incomeCatgories = catDao.queryForEq("group",
				Category.GROUP_INCOME);
		List<String> incomeCategoriesIds = new ArrayList<String>();
		for (Category income : incomeCatgories) {
			incomeCategoriesIds.add(income.getId());
		}
		return incomeCategoriesIds;
	}
	
	public List<String> getFixedExpensesCategoriesIds() throws SQLException {
		Dao<Category, String> catDao = this.getHelper().getCategories();
		QueryBuilder<Category, String> categoryQBuilder = catDao
				.queryBuilder();


		categoryQBuilder.where().eq("group",Category.GROUP_EXPENSE);
		categoryQBuilder.where().and().eq("isFixed", true);
		
		PreparedQuery<Category> categoryQuery = categoryQBuilder.prepare();
		
		
		List<Category> fixedExpensesCategoriesIds = catDao.query(categoryQuery);
		
		List<String> incomeCategoriesIds = new ArrayList<String>();
		for (Category income : fixedExpensesCategoriesIds) {
			incomeCategoriesIds.add(income.getId());
		}
		return incomeCategoriesIds;
	}
	
	public List<String> getUnFixedExpensesCategoriesIds() throws SQLException {
		Dao<Category, String> catDao = this.getHelper().getCategories();
		QueryBuilder<Category, String> categoryQBuilder = catDao
				.queryBuilder();


		categoryQBuilder.where().eq("group",Category.GROUP_EXPENSE);
		categoryQBuilder.where().and().eq("isFixed", false);
		
		PreparedQuery<Category> categoryQuery = categoryQBuilder.prepare();
		
		
		List<Category> fixedExpensesCategoriesIds = catDao.query(categoryQuery);
		
		List<String> incomeCategoriesIds = new ArrayList<String>();
		for (Category income : fixedExpensesCategoriesIds) {
			incomeCategoriesIds.add(income.getId());
		}
		return incomeCategoriesIds;
	}

}
