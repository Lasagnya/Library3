package com.project.library3.to.front;

import com.project.library3.to.banking.BankingTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatingTransactionResult {
	private BankingTransaction transaction;
	private ApiError apiError;
}
