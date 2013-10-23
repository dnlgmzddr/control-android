package com.banlinea.control.bussiness;

import java.sql.SQLException;

import android.content.Context;

import com.banlinea.control.entities.Category;
import com.banlinea.control.entities.util.GetAllBasicCateoriesResult;
import com.banlinea.control.local.DatabaseHelper;
import com.banlinea.control.remote.RemoteCategoryService;
import com.j256.ormlite.dao.Dao;

public class CategoryService extends BaseService {

	
	private RemoteCategoryService remoteCategorySerice;

	public CategoryService(Context context) {

		super(context);
		remoteCategorySerice = new RemoteCategoryService();
	}
	
	/**
	 * Import the defaults user categories.
	 * @throws SQLException
	 */
	public void ImportBaseCategories() throws SQLException{
		GetAllBasicCateoriesResult result = remoteCategorySerice.getAllBasics();
		if(result != null && result.isSuccessfullOperation()){
			DatabaseHelper helper = this.getHelper();
			Dao<Category, String> dao = helper.getCategories();
			for(Category category : result.getBody()){
				dao.create(category);
			}
		}
	}
}
