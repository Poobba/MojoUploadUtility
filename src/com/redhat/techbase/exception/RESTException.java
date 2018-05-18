package com.redhat.techbase.exception;

public class RESTException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RESTException(Exception e) {
		super(e);
	}
	
	public RESTException(String s) {
		super(s);
	}
	
	public RESTException(String s, Exception e) {
		super(s, e);
	}
}