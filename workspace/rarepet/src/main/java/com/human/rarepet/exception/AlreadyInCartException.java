package com.human.rarepet.exception;

public class AlreadyInCartException extends RuntimeException{
	
	public AlreadyInCartException(String message) {
		super(message);
	}
}
