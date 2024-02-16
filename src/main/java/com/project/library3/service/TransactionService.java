package com.project.library3.service;

import com.project.library3.domain.Person;
import com.project.library3.domain.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public interface TransactionService {
	Transaction save(Transaction transaction);
	Optional<Transaction> findOne(int id);
	void update(int id, Transaction updatedTransaction);
	void delete(int id);
	Transaction fillAndSave(Transaction transaction, Person debtor);
	void updateStatus(final Transaction updatedTransaction);
	ResponseEntity<?> createTransaction(Transaction transaction) throws ResponseStatusException;
}
