package com.augmentum.ot.exception;

public class ServerErrorException extends Exception {
	private static final long serialVersionUID = -1462714485890122558L;
	
	public ServerErrorException(String message,Throwable cause){
		super(message,cause);
	}
	public ServerErrorException(String message){
		super(message);
	}
	public ServerErrorException(Throwable cause) {
		super(cause);
	}
}
