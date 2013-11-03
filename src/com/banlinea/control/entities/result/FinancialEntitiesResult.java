package com.banlinea.control.entities.result;

import java.util.List;

import com.banlinea.control.dto.in.FinancialEntityDTO;
import com.banlinea.control.remote.util.CallResult;

public class FinancialEntitiesResult extends CallResult {

	private List<FinancialEntityDTO> body;

	public List<FinancialEntityDTO> getBody() {
		return body;
	}

	public void setBody(List<FinancialEntityDTO> body) {
		this.body = body;
	}
	
	
	
}
