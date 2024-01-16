package com.project.library3.models;

import com.project.library3.services.Views;
import com.project.library3.util.BankingConnection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.converter.json.MappingJacksonValue;

@Getter
@Setter
public class BankingResponsePage {
	private String htmlPage;

	private BankingResponsePage() {
	}

	public static BankingResponsePage createBankingResponsePage(BankingConnection bankingConnection, Transaction transaction) {
		BankingResponsePage bankingResponsePage = new BankingResponsePage();
		MappingJacksonValue jacksonValue = new MappingJacksonValue(transaction);
		jacksonValue.setSerializationView(Views.Dispatch.class);
		try {
			bankingResponsePage.setHtmlPage(bankingConnection.getConnection().body(jacksonValue).retrieve().body(String.class));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return bankingResponsePage;
	}
}
