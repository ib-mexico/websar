package com.ibmexico.libraries.notifications;

public class ApplicationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EnumException enumException;
	
	public ApplicationException(EnumException enumException) {
		this.enumException = enumException;
	}
	
	public String getCode() {
		return enumException.getCode();
	}
	
	public String getTitle() {
		return enumException.getTitle();
	}
	
	public String getSimpleMessage() {
		return enumException.getSimpleMessage();
	}
	
	public String getMessage() {
		return enumException.getMessage();
	}
}
