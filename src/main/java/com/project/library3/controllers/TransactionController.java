package com.project.library3.controllers;

import com.project.library3.models.*;
import com.project.library3.services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
	private final TransactionsService transactionsService;

	@Autowired
	public TransactionController(TransactionsService transactionsService) {
		this.transactionsService = transactionsService;
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public CreatingTransactionResult sendTransaction(@RequestBody Transaction transaction) {
		return transactionsService.createTransaction(transaction);
	}
//	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//	public String sendTransaction(Model model, @ModelAttribute("transaction") Transaction transaction) {
//		Optional<Person> debtorOptional = peopleService.findOne(transaction.getDebtor().getId());
//		if (debtorOptional.isPresent()) {
//			Person debtor = debtorOptional.get();
//			transaction = transactionsService.fillAndSave(transaction, debtor);
//			BankingResponsePage bankingResponsePage = BankingResponsePage.createBankingResponsePage(this::getBankingConnection, transaction);
//			return bankingResponsePage.getHtmlPage();
//		}
//		else {
//			model.addAttribute("id", transaction.getDebtor());
//			return "people/incorrect_id";
//		}
//	}
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
		transactionsService.updateStatus(transaction);
	}
}
