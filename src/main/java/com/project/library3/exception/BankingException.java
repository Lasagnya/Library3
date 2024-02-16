package com.project.library3.exception;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@JsonIncludeProperties({"body"})
public class BankingException extends ResponseStatusException {

	public BankingException() {
		super(HttpStatus.CONFLICT, "Incorrect bank, account or amount of transaction");
	}

	public BankingException(String reason) {
		super(HttpStatus.CONFLICT, reason);
	}
}
