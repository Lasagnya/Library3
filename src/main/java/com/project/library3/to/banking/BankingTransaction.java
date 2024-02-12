package com.project.library3.to.banking;

import com.fasterxml.jackson.annotation.JsonView;
import com.project.library3.domain.Transaction;
import com.project.library3.enumeration.Currency;
import com.project.library3.enumeration.TransactionStatus;
import com.project.library3.util.Views;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class BankingTransaction {
	private int id;

	@JsonView(Views.Dispatch.class)
	private int invoiceId;

	private Date time;

	private int sendingBank;

	@JsonView(Views.Dispatch.class)
	private int receivingBank;

	@JsonView(Views.Dispatch.class)
	private int sendingAccount;

	@JsonView(Views.Dispatch.class)
	private int receivingAccount;

	@JsonView(Views.Dispatch.class)
	private double amount;

	@JsonView(Views.Dispatch.class)
	private Currency currency;

	private TransactionStatus status;

	@JsonView(Views.Dispatch.class)
	private String callbackUri = "http://localhost:8080/transaction/callback";

	public BankingTransaction(Transaction transactionDb) {
		this.id = transactionDb.getId();
		this.invoiceId = transactionDb.getInvoiceId();
		time = transactionDb.getTime();
		sendingBank = transactionDb.getSendingBank();
		sendingAccount = transactionDb.getSendingAccount();
		receivingBank = transactionDb.getReceivingBank();
		receivingAccount = transactionDb.getReceivingAccount();
		amount = transactionDb.getAmount();
		currency = transactionDb.getCurrency();
		status = transactionDb.getStatus();
		this.callbackUri = transactionDb.getCallbackUri();
	}
}
