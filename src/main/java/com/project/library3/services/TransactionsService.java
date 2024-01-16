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
	private final PeopleService peopleService;

	@Autowired
	public TransactionsService(TransactionsRepository transactionsRepository, PeopleService peopleService) {
		this.transactionsRepository = transactionsRepository;
		this.peopleService = peopleService;
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

	public void updateStatus(final Transaction updatedTransaction) {
		switch (updatedTransaction.getStatus()) {
			case NEW, PENDING -> {}

			case PAID -> {
				findOne(updatedTransaction.getInvoiceId()).ifPresent(transaction ->
						peopleService.writeOffFine(transaction.getDebtor(), updatedTransaction.getAmount()));
				update(updatedTransaction.getInvoiceId(), updatedTransaction);
			}

			case INVALID, CANCELED, EXPIRED, REFUNDED -> update(updatedTransaction.getInvoiceId(), updatedTransaction);
		}
	}
}
