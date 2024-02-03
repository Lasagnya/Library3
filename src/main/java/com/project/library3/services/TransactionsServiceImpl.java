package com.project.library3.services;

import com.project.library3.models.*;
import com.project.library3.repositories.TransactionsRepository;
import com.project.library3.util.TransactionsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TransactionsServiceImpl implements TransactionsService {
	private final TransactionsRepository transactionsRepository;
	private final PeopleService peopleService;
	private final TransactionsClient transactionsClient;

	@Autowired
	public TransactionsServiceImpl(TransactionsRepository transactionsRepository, PeopleService peopleService, TransactionsClient transactionsClient) {
		this.transactionsRepository = transactionsRepository;
		this.peopleService = peopleService;
		this.transactionsClient = transactionsClient;
	}

	@Override
	public Transaction save(Transaction transaction) {
		return transactionsRepository.save(transaction);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Transaction> findOne(int id) {
		return transactionsRepository.findById(id);
	}

	@Override
	public void update(int id, Transaction updatedTransaction) {
		findOne(id).ifPresent(transactionToBeUpdated -> {
			updatedTransaction.setInvoiceId(id);
			updatedTransaction.setDebtor(transactionToBeUpdated.getDebtor());
			transactionsRepository.save(updatedTransaction);
		});
	}

	@Override
	public void delete(int id) {
		transactionsRepository.deleteById(id);
	}

	@Override
	public Transaction fillAndSave(Transaction transaction, Person debtor) {
		transaction.setReceivingAccount(12345678);
		transaction.setReceivingBank(1);
		transaction.setAmount(debtor.getFine());
		transaction.setCurrency(Currency.BYN);
		transaction.setStatus(TransactionStatus.PENDING);
		return save(transaction);
	}

	@Override
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

	@Override
	public CreatingTransactionResult createTransaction(Transaction transaction) {
		Optional<Person> debtorOptional = peopleService.findOne(transaction.getDebtor().getId());
		if (debtorOptional.isPresent()) {
			Person debtor = debtorOptional.get();
			transaction = fillAndSave(transaction, debtor);
			Transaction callback = transactionsClient.createTransaction(transaction);
			CreatingTransactionResult result = new CreatingTransactionResult(callback, new ApiError(0));
			if (callback.getStatus() == TransactionStatus.INVALID) {
				transaction.setStatus(TransactionStatus.INVALID);
				updateStatus(transaction);
				result.setApiError(new ApiError(2));
			}
			return result;
		}
		else {
			return new CreatingTransactionResult(transaction, new ApiError(1));
		}
	}
}
