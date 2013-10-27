package com.banlinea.control.remote;

import java.util.concurrent.ExecutionException;

import com.banlinea.control.entities.BaseEntity;
import com.banlinea.control.entities.util.GetAllBasicCateoriesResult;
import com.banlinea.control.remote.util.ApiMethod;
import com.banlinea.control.remote.util.ControlApiHandler;

public class RemoteCategoryService {

	public GetAllBasicCateoriesResult getAllBasics(){
	
		GetAllBasicCateoriesResult result = null;
		
		try {
			
			result = new ControlApiHandler<GetAllBasicCateoriesResult,BaseEntity>(
						null,
						ApiMethod.CATS_GET_ALL,
						GetAllBasicCateoriesResult.class
						).execute().get();
		
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
