package com.banlinea.control.remote.util;

public class CallResult{
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
