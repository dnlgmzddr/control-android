package com.banlinea.control.remote.util;

public class CallResult{
	

	protected boolean successfullOperation;
	protected String message;
	
	
	public CallResult(){
		
	}
	public CallResult(boolean successfullOperation) {
		super();
		this.successfullOperation = successfullOperation;
	}
	public CallResult(boolean successfullOperation, String message) {
		super();
		this.successfullOperation = successfullOperation;
		this.message = message;
	}
	
	
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isSuccessfullOperation() {
		return successfullOperation;
	}
	public void setSuccessfullOperation(boolean successfullOperation) {
		this.successfullOperation = successfullOperation;
	}
}
