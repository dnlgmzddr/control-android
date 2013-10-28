package com.banlinea.control.entities.result;

import com.banlinea.control.entities.Category;
import com.banlinea.control.remote.util.CallResult;

public class CategoryResult extends CallResult {

	
	public Category getBody() {
		return Body;
	}

	public void setBody(Category body) {
		Body = body;
	}

	private Category Body;
	
	

}
