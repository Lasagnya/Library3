package com.project.library3.util;

import com.project.library3.models.CreatingTransactionResult;
import com.project.library3.models.Transaction;
import org.springframework.web.client.RestClient;

public interface TransactionsClient {
	CreatingTransactionResult createTransaction(Transaction transaction);
}
