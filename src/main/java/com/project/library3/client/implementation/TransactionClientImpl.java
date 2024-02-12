package com.project.library3.client.implementation;

import com.project.library3.client.TransactionClient;
import com.project.library3.domain.Transaction;
import com.project.library3.to.banking.BankingTransaction;
import com.project.library3.util.Views;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class TransactionClientImpl implements TransactionClient {
	private RestClient getBankingConnection() {
		return RestClient.create("http://localhost:7070/api/transaction/pay");
	}

	@Override
	public Transaction createTransaction(Transaction transaction) {
		MappingJacksonValue jacksonValue = new MappingJacksonValue(new BankingTransaction(transaction));
		jacksonValue.setSerializationView(Views.Dispatch.class);
		return getBankingConnection().post().contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).body(jacksonValue)
				.retrieve().body(Transaction.class);
	}
}
