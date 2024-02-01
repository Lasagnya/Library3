package com.project.library3.services;

import com.project.library3.models.CreatingTransactionResult;
import com.project.library3.models.Person;
import com.project.library3.models.Transaction;

import java.util.Optional;

public interface TransactionsService {
	Transaction save(Transaction transaction);
	Optional<Transaction> findOne(int id);
	void update(int id, Transaction updatedTransaction);
	void delete(int id);
	Transaction fillAndSave(Transaction transaction, Person debtor);
	void updateStatus(final Transaction updatedTransaction);
	CreatingTransactionResult createTransaction(Transaction transaction);
}
