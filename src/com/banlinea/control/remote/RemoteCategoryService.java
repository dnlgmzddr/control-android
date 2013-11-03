package com.banlinea.control.remote;

import java.util.concurrent.ExecutionException;

import com.banlinea.control.entities.Category;
import com.banlinea.control.entities.result.CategoryResult;
import com.banlinea.control.entities.result.GetAllBasicCateoriesResult;
import com.banlinea.control.remote.util.ApiMethod;
import com.banlinea.control.remote.util.ControlApiHandler;

public class RemoteCategoryService {

	public GetAllBasicCateoriesResult getAllBasics() {

		GetAllBasicCateoriesResult result = null;

		try {

			result = new ControlApiHandler<GetAllBasicCateoriesResult, Object>(
					null, ApiMethod.CATS_GET_ALL,
					GetAllBasicCateoriesResult.class).execute().get();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public CategoryResult addCustom(Category userCategory) {

		CategoryResult result = null;

		try {

			result = new ControlApiHandler<CategoryResult, Category>(
					userCategory, ApiMethod.CATS_ADD, CategoryResult.class)
					.execute().get();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
}
