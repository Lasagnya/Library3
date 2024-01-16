package com.project.library3.models;

import com.fasterxml.jackson.annotation.JsonView;
import com.project.library3.services.Views;
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

	@JsonView(Views.Dispatch.class)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int invoiceId;

	@JoinColumn(name = "debtor", referencedColumnName = "id")
	@ManyToOne
	private Person debtor;

	/** поле банк-получатель */
	@JsonView(Views.Dispatch.class)
	@Column(name = "receiving_bank")
	private int receivingBank;

	/** поле аккаунт-отправитель */
	@JsonView(Views.Dispatch.class)
	@Column(name = "sending_account")
	private int sendingAccount;

	/** поле аккаунт-получатель */
	@JsonView(Views.Dispatch.class)
	@Column(name = "receiving_account")
	private int receivingAccount;

	/** поле сумма транзакции */
	@JsonView(Views.Dispatch.class)
	@Column(name = "amount")
	private double amount;

	/** поле валюта транзации */
	@JsonView(Views.Dispatch.class)
	@Column(name = "transaction_currency")
	@Enumerated(EnumType.STRING)
	private Currency currency;

	@JsonView(Views.Dispatch.class)
	@Column(name = "transaction_status")
	@Enumerated(EnumType.STRING)
	private TransactionStatus status;

	@JsonView(Views.Dispatch.class)
	@Transient
	private String callbackUri = "http://localhost:8080/transaction/callback";

	public Transaction(Person debtor) {
		this.debtor = debtor;
	}
}
