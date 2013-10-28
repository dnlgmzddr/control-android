package com.banlinea.control.entities.result;

import java.util.ArrayList;

import com.banlinea.control.entities.Category;
import com.banlinea.control.remote.util.CallResult;

public class GetAllBasicCateoriesResult extends CallResult{
	
	
	
	private ArrayList<Category> Body;

	public ArrayList<Category> getBody() {
		return Body;
	}

	public void setBody(ArrayList<Category> body) {
		Body = body;
	}
	
}