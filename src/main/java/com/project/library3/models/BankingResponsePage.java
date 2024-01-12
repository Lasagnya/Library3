package com.project.library3.models;

import com.project.library3.util.BankingConnection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankingResponsePage {
	private String htmlPage;

	private BankingResponsePage() {
	}

	public static BankingResponsePage createBankingResponsePage(BankingConnection bankingConnection, Transaction transaction) {
		BankingResponsePage bankingResponsePage = new BankingResponsePage();
		bankingResponsePage.setHtmlPage(bankingConnection.getConnection().body(transaction).retrieve().body(String.class));
		return bankingResponsePage;
	}
}
