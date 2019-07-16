package com.lsiqueira.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserBadRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserBadRequestException(String exception) {
		super(exception);
	}

}
