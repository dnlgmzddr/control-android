package com.banlinea.control.bussiness;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.banlinea.control.entities.Category;
import com.banlinea.control.entities.util.GetAllBasicCateoriesResult;
import com.banlinea.control.local.DatabaseHelper;
import com.banlinea.control.remote.RemoteCategoryService;
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
	
	/**
	 * 
	 * @param group
	 * @return
	 * @throws SQLException
	 */
	public List<Category> GetParentCategoriesPerGroup(int group) throws SQLException{
		DatabaseHelper helper = this.getHelper();
		Dao<Category, String> dao = helper.getCategories();
		
		
		QueryBuilder<Category, String> queryBuilder =
				dao.queryBuilder();
		
		queryBuilder.where().eq("IdParent", Category.SYSTEM_EMPTY_ID)
		.and().eq("Group",group);
		
		
		PreparedQuery<Category> preparedQuery = queryBuilder.prepare();
		
		List<Category> categories = dao.query(preparedQuery);
		
		
		return categories;
	}
	/**
	 * Look up for the child categories of the desired category.
	 * @param idParentCategory , Id of the parent category.
	 * @return The list of categories that have the parent passed as argument.
	 * @throws SQLException
	 */
	public List<Category> GetChilds(String idParentCategory) throws SQLException{
		DatabaseHelper helper = this.getHelper();
		Dao<Category, String> dao = helper.getCategories();
		
		
		QueryBuilder<Category, String> queryBuilder =
				dao.queryBuilder();
		
		queryBuilder.where().eq("IdParent", idParentCategory);
		
		
		
		PreparedQuery<Category> preparedQuery = queryBuilder.prepare();
		
		List<Category> categories = dao.query(preparedQuery);
		
		
		return categories;
	}
	
	public boolean EditCategory(){
		return false;
	}
}
