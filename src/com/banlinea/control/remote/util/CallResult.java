package com.banlinea.control.remote.util;

public class CallResult{
	
	
	
	public CallResult(){
		
	}
	public CallResult(boolean successfullOperation) {
		super();
		SuccessfullOperation = successfullOperation;
	}
	public CallResult(boolean successfullOperation, String message) {
		super();
		SuccessfullOperation = successfullOperation;
		Message = message;
	}
	protected boolean SuccessfullOperation;
	protected String Message;
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public boolean isSuccessfullOperation() {
		return SuccessfullOperation;
	}
	public void setSuccessfullOperation(boolean successfullOperation) {
		SuccessfullOperation = successfullOperation;
	}
}
