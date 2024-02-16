package com.project.library3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.library3.controller.TransactionController;
import com.project.library3.enumeration.Currency;
import com.project.library3.domain.Person;
import com.project.library3.domain.Transaction;
import com.project.library3.enumeration.TransactionStatus;
import com.project.library3.repository.PersonRepository;
import com.project.library3.repository.TransactionRepository;
import com.project.library3.service.*;
import com.project.library3.service.implementation.PersonServiceImpl;
import com.project.library3.service.PersonService;
import com.project.library3.service.implementation.TransactionServiceImpl;
import com.project.library3.client.TransactionClient;
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

	@SpyBean(TransactionServiceImpl.class)
	private TransactionService transactionService;

	@SpyBean(PersonServiceImpl.class)
	private PersonService personService;

	@MockBean
	private TransactionClient transactionClient;

	@MockBean
	private TransactionRepository transactionRepository;

	@MockBean
	private PersonRepository personRepository;

	private final Person debtor = new Person(3, "", 0, 100.0, null);

	private final Transaction transaction = new Transaction(9, debtor, 1234, 12345678, 1, debtor.getFine(), Currency.BYN, TransactionStatus.PENDING, "");

	@Test
	public void sendTransaction_succeed() throws Exception {
		Mockito.when(personRepository.findById(3)).thenReturn(Optional.of(debtor));
		Mockito.when(transactionRepository.save(transaction)).thenReturn(transaction);
		Mockito.when(transactionClient.createTransaction(any())).thenReturn(transaction);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
				.post("/transaction")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(transaction));
		mockMvc.perform(mockRequest)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.invoiceId", is(transaction.getInvoiceId())))
				.andExpect(jsonPath("$.status", is(TransactionStatus.PENDING.toString())))
				.andExpect(jsonPath("$.amount", is(transaction.getAmount())))
				.andDo(print());
	}

	@Test
	public void sendTransaction_incorrectDebtor() throws Exception {
		Mockito.when(personRepository.findById(3)).thenReturn(Optional.empty());
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
				.post("/transaction")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(transaction));
		mockMvc.perform(mockRequest)
				.andExpect(status().isConflict())
				.andDo(print());
	}

	@Test
	public void sendTransaction_incorrectAccount() throws Exception {
		Transaction callback = new Transaction(0, null, 1234, 12345678, 1, debtor.getFine(), Currency.BYN, TransactionStatus.INVALID, "");
		Mockito.when(personRepository.findById(3)).thenReturn(Optional.of(debtor));
		Mockito.when(transactionRepository.save(any())).thenReturn(transaction);
		Mockito.when(transactionClient.createTransaction(any())).thenReturn(callback);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
				.post("/transaction")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(transaction));
		mockMvc.perform(mockRequest)
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.transaction.status", is(TransactionStatus.INVALID.toString())))
				.andExpect(jsonPath("$.transaction.amount", is(transaction.getAmount())))
				.andDo(print());
	}
}
