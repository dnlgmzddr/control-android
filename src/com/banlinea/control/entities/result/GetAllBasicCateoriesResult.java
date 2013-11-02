package com.banlinea.control.entities.result;

import java.util.ArrayList;

import com.banlinea.control.entities.Category;
import com.banlinea.control.remote.util.CallResult;

public class GetAllBasicCateoriesResult extends CallResult{
	
	
	
	private ArrayList<Category> body;

	public ArrayList<Category> getBody() {
		return body;
	}

	public void setBody(ArrayList<Category> body) {
		this.body = body;
	}
	
}