package com.project.library3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.library3.controllers.TransactionController;
import com.project.library3.models.Currency;
import com.project.library3.models.Person;
import com.project.library3.models.Transaction;
import com.project.library3.models.TransactionStatus;
import com.project.library3.repositories.PeopleRepository;
import com.project.library3.repositories.TransactionsRepository;
import com.project.library3.services.PeopleService;
import com.project.library3.services.PeopleServiceImpl;
import com.project.library3.services.TransactionsService;
import com.project.library3.services.TransactionsServiceImpl;
import com.project.library3.util.TransactionsClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc
public class TransactionControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@SpyBean(TransactionsServiceImpl.class)
	private TransactionsService transactionsService;

	@SpyBean(PeopleServiceImpl.class)
	private PeopleService peopleService;

	@MockBean
	private TransactionsClient transactionsClient;

	@MockBean
	private TransactionsRepository transactionsRepository;

	@MockBean
	private PeopleRepository peopleRepository;

	private final Person debtor = new Person(3, "", 0, 100.0, null);

	private final Transaction transaction = new Transaction(0, debtor, 1234, 12345678, 1, debtor.getFine(), Currency.BYN, TransactionStatus.PENDING, "");

	@Test
	public void sendTransaction_succeed() throws Exception {
		Mockito.when(peopleRepository.findById(3)).thenReturn(Optional.of(debtor));
		Mockito.when(transactionsRepository.save(transaction)).thenReturn(transaction);
		Mockito.when(transactionsClient.createTransaction(any())).thenReturn(transaction);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
				.post("/transaction")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(transaction));
		mockMvc.perform(mockRequest)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.apiError.errorId", is(0)))
				.andExpect(jsonPath("$.transaction.debtor.id", is(transaction.getDebtor().getId())))
				.andExpect(jsonPath("$.transaction.status", is(TransactionStatus.PENDING.toString())))
				.andExpect(jsonPath("$.transaction.amount", is(transaction.getAmount())))
				.andDo(print());
	}

	@Test
	public void sendTransaction_incorrectDebtor() throws Exception {
		Mockito.when(peopleRepository.findById(3)).thenReturn(Optional.empty());
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
				.post("/transaction")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(transaction));
		mockMvc.perform(mockRequest)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.apiError.errorId", is(1)))
				.andExpect(jsonPath("$.transaction.amount", is(transaction.getAmount())))
				.andDo(print());
	}

	@Test
	public void sendTransaction_incorrectAccount() throws Exception {
		Transaction callback = new Transaction(0, null, 1234, 12345678, 1, debtor.getFine(), Currency.BYN, TransactionStatus.INVALID, "");
		Mockito.when(peopleRepository.findById(3)).thenReturn(Optional.of(debtor));
		Mockito.when(transactionsRepository.save(any())).thenReturn(transaction);
		Mockito.when(transactionsClient.createTransaction(any())).thenReturn(callback);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
				.post("/transaction")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(transaction));
		mockMvc.perform(mockRequest)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.apiError.errorId", is(2)))
				.andExpect(jsonPath("$.transaction.status", is(TransactionStatus.INVALID.toString())))
				.andExpect(jsonPath("$.transaction.amount", is(transaction.getAmount())))
				.andDo(print());
	}
}
