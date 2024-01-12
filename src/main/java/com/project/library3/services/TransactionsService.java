package com.project.library3.services;

import com.project.library3.models.Currency;
import com.project.library3.models.Person;
import com.project.library3.models.Transaction;
import com.project.library3.models.TransactionStatus;
import com.project.library3.repositories.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TransactionsService {
	private final TransactionsRepository transactionsRepository;

	@Autowired
	public TransactionsService(TransactionsRepository transactionsRepository) {
		this.transactionsRepository = transactionsRepository;
	}

	public Transaction save(Transaction transaction) {
		return transactionsRepository.save(transaction);
	}

	@Transactional(readOnly = true)
	public Optional<Transaction> findOne(int id) {
		return transactionsRepository.findById(id);
	}

	public void update(int id, Transaction updatedTransaction) {
		findOne(id).ifPresent(transactionToBeUpdated -> {
			updatedTransaction.setInvoiceId(id);
			updatedTransaction.setDebtor(transactionToBeUpdated.getDebtor());
			transactionsRepository.save(updatedTransaction);
		});
//		Transaction transactionToBeUpdated = findOne(id).get();
//		updatedTransaction.setInvoice_id(id);
//		updatedTransaction.setDebtor(transactionToBeUpdated.getDebtor());
//		transactionsRepository.save(updatedTransaction);
	}

	public void delete(int id) {
		transactionsRepository.deleteById(id);
	}

	public Transaction fillAndSave(Transaction transaction, Person debtor) {
		transaction.setReceivingAccount(12345678);
		transaction.setReceivingBank(1);
		transaction.setAmount(debtor.getFine());
		transaction.setCurrency(Currency.BYN);
		transaction.setStatus(TransactionStatus.PENDING);
		return save(transaction);
	}
}
