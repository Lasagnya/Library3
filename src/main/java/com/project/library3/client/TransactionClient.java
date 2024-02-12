package com.project.library3.client;

import com.project.library3.domain.Transaction;

public interface TransactionClient {
	Transaction createTransaction(Transaction transaction);
}
