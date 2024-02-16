package com.project.library3.exception;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

//@ResponseStatus(HttpStatus.CONFLICT)
@JsonIncludeProperties({"body"})
public class DebtorException extends ResponseStatusException {
	public DebtorException() {
		super(HttpStatus.CONFLICT, "Incorrect debtor");
	}

	public DebtorException(String message) {
		super(HttpStatus.CONFLICT, message);
	}
}
