package com.project.library3.service.implementation;

import com.project.library3.to.front.ApiError;
import com.project.library3.to.front.CreatingTransactionResult;
import com.project.library3.domain.Person;
import com.project.library3.domain.Transaction;
import com.project.library3.enumeration.Currency;
import com.project.library3.enumeration.TransactionStatus;
import com.project.library3.repository.TransactionRepository;
import com.project.library3.service.PersonService;
import com.project.library3.service.TransactionService;
import com.project.library3.client.TransactionClient;
import com.project.library3.to.banking.BankingTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {
	private final TransactionRepository transactionRepository;
	private final PersonService personService;
	private final TransactionClient transactionClient;

	@Autowired
	public TransactionServiceImpl(TransactionRepository transactionRepository, PersonService personService, TransactionClient transactionClient) {
		this.transactionRepository = transactionRepository;
		this.personService = personService;
		this.transactionClient = transactionClient;
	}

	@Override
	public Transaction save(Transaction transaction) {
		return transactionRepository.save(transaction);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Transaction> findOne(int id) {
		return transactionRepository.findById(id);
	}

	@Override
	public void update(int id, Transaction updatedTransaction) {
		findOne(id).ifPresent(transactionToBeUpdated -> {
			updatedTransaction.setInvoiceId(id);
			updatedTransaction.setDebtor(transactionToBeUpdated.getDebtor());
			transactionRepository.save(updatedTransaction);
		});
	}

	@Override
	public void delete(int id) {
		transactionRepository.deleteById(id);
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
						personService.writeOffFine(transaction.getDebtor(), updatedTransaction.getAmount()));
				update(updatedTransaction.getInvoiceId(), updatedTransaction);
			}

			case INVALID, CANCELED, EXPIRED, REFUNDED -> update(updatedTransaction.getInvoiceId(), updatedTransaction);
		}
	}

	@Override
	public CreatingTransactionResult createTransaction(Transaction transaction) {
		Optional<Person> debtorOptional = personService.findOne(transaction.getDebtor().getId());
		if (debtorOptional.isPresent()) {
			Person debtor = debtorOptional.get();
			transaction = fillAndSave(transaction, debtor);
			Transaction callback = transactionClient.createTransaction(transaction);
			CreatingTransactionResult result = new CreatingTransactionResult(new BankingTransaction(callback), new ApiError(0));
			if (callback.getStatus() == TransactionStatus.INVALID) {
				transaction.setStatus(TransactionStatus.INVALID);
				updateStatus(transaction);
				result.setApiError(new ApiError(2));
			}
			return result;
		}
		else {
			return new CreatingTransactionResult(new BankingTransaction(transaction), new ApiError(1));
		}
	}
}
