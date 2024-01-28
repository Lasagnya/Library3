package com.project.library3.util;

import com.project.library3.models.CreatingTransactionResult;
import com.project.library3.models.Transaction;
import com.project.library3.services.PeopleService;
import com.project.library3.services.Views;
import com.project.library3.util.TransactionsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class TransactionsClientImpl implements TransactionsClient {
	private final PeopleService peopleService;

	@Autowired
	public TransactionsClientImpl(PeopleService peopleService) {
		this.peopleService = peopleService;
	}

	private RestClient getBankingConnection() {
		return RestClient.create("http://localhost:7070/api/transaction/pay");
	}

	@Override
	public CreatingTransactionResult createTransaction(Transaction transaction) {
		MappingJacksonValue jacksonValue = new MappingJacksonValue(transaction);
		jacksonValue.setSerializationView(Views.Dispatch.class);
		return getBankingConnection().post().contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).body(jacksonValue)
				.retrieve().body(CreatingTransactionResult.class);
	}
}
