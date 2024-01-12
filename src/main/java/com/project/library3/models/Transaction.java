package com.project.library3.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *  Класс транзакций
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "transaction")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int invoiceId;

	@JsonIgnore
	@JoinColumn(name = "debtor", referencedColumnName = "id")
	@ManyToOne
	private Person debtor;

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
	@JsonIgnore
	private TransactionStatus status;

	@Transient
	@JsonInclude
	private String callbackUri = "http://localhost:8080/transaction/callback";

	public Transaction(Person debtor) {
		this.debtor = debtor;
	}
}
