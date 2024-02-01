package com.project.library3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.library3.models.Person;
import com.project.library3.models.Transaction;
import com.project.library3.models.TransactionStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc
public class TransactionControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@Test
	public void sendTransaction_succeed() throws Exception {
		Transaction transaction = new Transaction();
		transaction.setDebtor(new Person(3));
		transaction.setSendingAccount(1234);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
				.post("/transaction")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(transaction));
		mockMvc.perform(mockRequest)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.apiError.errorId", is(0)))
				.andExpect(jsonPath("$.transaction.status", is(TransactionStatus.PENDING.toString())))
				.andExpect(jsonPath("$.transaction.amount", is(100.0)));
	}

	@Test
	public void sendTransaction_incorrectDebtor() throws Exception {
		Transaction transaction = new Transaction();
		transaction.setDebtor(new Person(1));
		transaction.setSendingAccount(1234);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
				.post("/transaction")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(transaction));
		mockMvc.perform(mockRequest)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.apiError.errorId", is(1)));
	}

	@Test
	public void sendTransaction_incorrectAccount() throws Exception {
		Transaction transaction = new Transaction();
		transaction.setDebtor(new Person(3));
		transaction.setSendingAccount(0);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
				.post("/transaction")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(transaction));
		mockMvc.perform(mockRequest)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.apiError.errorId", is(2)))
				.andExpect(jsonPath("$.transaction.status", is(TransactionStatus.INVALID.toString())))
				.andExpect(jsonPath("$.transaction.receivingAccount", is(12345678)));
	}
}
