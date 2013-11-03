package com.banlinea.control.remote;

import java.util.concurrent.ExecutionException;

import com.banlinea.control.dto.out.FinancialEntitiesRequest;
import com.banlinea.control.dto.out.ProductFilterRequest;
import com.banlinea.control.entities.UserFinancialProduct;
import com.banlinea.control.entities.result.FinancialEntitiesResult;
import com.banlinea.control.entities.result.FinancialProductResult;
import com.banlinea.control.remote.util.ApiMethod;
import com.banlinea.control.remote.util.CallResult;
import com.banlinea.control.remote.util.ControlApiHandler;

public class RemoteFinancialProductService {

	public FinancialEntitiesResult getFinancialEntitiesByType(
			FinancialEntitiesRequest request) {

		FinancialEntitiesResult result = null;

		try {
			result = new ControlApiHandler<FinancialEntitiesResult, FinancialEntitiesRequest>(
					request, ApiMethod.PRODUCTS_GET_ENTITIES,
					FinancialEntitiesResult.class).execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public FinancialProductResult GetFiltered(ProductFilterRequest filter) {
		FinancialProductResult result = null;

		try {
			result = new ControlApiHandler<FinancialProductResult, ProductFilterRequest>(
					filter, ApiMethod.PRODUCTS_GET_FILTERED,
					FinancialProductResult.class).execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public CallResult AddProductToUser(UserFinancialProduct product) {
		CallResult result = new CallResult(false);

		try {
			result = new ControlApiHandler<CallResult, UserFinancialProduct>(
					product, ApiMethod.PRODUCTS_GET_ADD_TO_USER, CallResult.class)
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
