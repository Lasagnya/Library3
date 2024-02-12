package com.project.library3.to.front;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiError {
	private int errorId;												// TODO сделать полноценную реализацию

	public ApiError(int errorId) {
		this.errorId = errorId;
	}
}
