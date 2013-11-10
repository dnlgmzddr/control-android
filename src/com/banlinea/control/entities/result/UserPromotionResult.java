package com.banlinea.control.entities.result;

import java.util.List;

import com.banlinea.control.entities.Promotion;
import com.banlinea.control.remote.util.CallResult;

public class UserPromotionResult extends CallResult {

	
	public List<Promotion> getBody() {
		return body;
	}

	public void setBody(List<Promotion> body) {
		this.body = body;
	}

	private List<Promotion> body;
	
	

}
