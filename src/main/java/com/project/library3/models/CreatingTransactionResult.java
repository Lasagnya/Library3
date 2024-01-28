package com.project.library3.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreatingTransactionResult {
	private int id;
	private ApiError apiError;

	public CreatingTransactionResult(int id) {
		this.id = id;
	}

	public CreatingTransactionResult(ApiError apiError) {
		this.apiError = apiError;
	}
}
