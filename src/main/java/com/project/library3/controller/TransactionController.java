package com.project.library3.controller;

import com.project.library3.domain.Transaction;
import com.project.library3.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
	private final TransactionService transactionService;

	@Autowired
	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> sendTransaction(@RequestBody Transaction transaction) throws ResponseStatusException {
		return transactionService.createTransaction(transaction);
	}
	//			Работает, пересылает post, но изменить тело запроса невозможно
//			request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
//			ModelAndView modelAndView = new ModelAndView();
//			modelAndView.setViewName("redirect:http://localhost:7070/api/transaction/pay");
//			return modelAndView;

	//			Это всё добавляет параметры в url
//			ObjectMapper objectMapper = new ObjectMapper();
//			Map<String, String> map = objectMapper.convertValue(transaction, Map.class);
//			modelAndView.getModelMap().addAllAttributes(map);
//			Logger.getGlobal().info(modelAndView.getModelMap().toString());
//			modelAndView.addObject("testAttribute", "qwerty");

	@PostMapping("/callback")
	@ResponseStatus(value = HttpStatus.OK)
	public void getResult(@RequestBody Transaction transaction) {
		transactionService.updateStatus(transaction);
	}
}
