package com.project.library3.to.front;

import com.project.library3.to.banking.BankingTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.server.ResponseStatusException;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatingTransactionResult {
	private BankingTransaction transaction;
	private ResponseStatusException exception;
}
