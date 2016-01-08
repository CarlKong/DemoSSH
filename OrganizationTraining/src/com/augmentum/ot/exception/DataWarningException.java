package com.augmentum.ot.exception;

public class DataWarningException extends Exception {
	private static final long serialVersionUID = -1462714485890122558L;
	
	public DataWarningException(String message,Throwable cause){
		super(message,cause);
	}
	public DataWarningException(String message){
		super(message);
	}
	public DataWarningException(Throwable cause) {
		super(cause);
	}
}
