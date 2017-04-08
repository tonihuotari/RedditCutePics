package com.sombrero.huotari.redditcutepics.net;

public class ApiErrorException extends Exception {
	private int mStatusCode;

	public ApiErrorException(String message, int statusCode) {
		super(message);
		mStatusCode = statusCode;
	}

	public ApiErrorException(String message) {
		super(message);
	}
}
