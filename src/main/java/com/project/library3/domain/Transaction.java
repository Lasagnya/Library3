package com.project.library3.domain;

import com.project.library3.enumeration.Currency;
import com.project.library3.enumeration.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 *  Класс транзакций
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "transaction")
public class Transaction {

	@Transient
	private int id;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int invoiceId;

	@Transient
	private Date time;

	@JoinColumn(name = "debtor", referencedColumnName = "id")
	@ManyToOne
	private Person debtor;

	@Transient
	private int sendingBank;

	/** поле банк-получатель */
	@Column(name = "receiving_bank")
	private int receivingBank;

	/** поле аккаунт-отправитель */
	@Column(name = "sending_account")
	private int sendingAccount;

	/** поле аккаунт-получатель */
	@Column(name = "receiving_account")
	private int receivingAccount;

	/** поле сумма транзакции */
	@Column(name = "amount")
	private double amount;

	/** поле валюта транзации */
	@Column(name = "transaction_currency")
	@Enumerated(EnumType.STRING)
	private Currency currency;

	@Column(name = "transaction_status")
	@Enumerated(EnumType.STRING)
	private TransactionStatus status;

	@Transient
	private String callbackUri = "http://localhost:8080/transaction/callback";

	public Transaction(Person debtor) {
		this.debtor = debtor;
	}

	public Transaction(int invoiceId, Person debtor, int receivingBank, int sendingAccount, int receivingAccount, double amount, Currency currency, TransactionStatus status, String callbackUri) {
		this.invoiceId = invoiceId;
		this.debtor = debtor;
		this.receivingBank = receivingBank;
		this.sendingAccount = sendingAccount;
		this.receivingAccount = receivingAccount;
		this.amount = amount;
		this.currency = currency;
		this.status = status;
		this.callbackUri = callbackUri;
	}
}
