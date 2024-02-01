package com.project.library3.util;

import com.project.library3.models.Transaction;

public interface TransactionsClient {
	Transaction createTransaction(Transaction transaction);
}
