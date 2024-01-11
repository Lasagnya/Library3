package com.project.library3.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.library3.models.Currency;
import com.project.library3.models.Person;
import com.project.library3.models.Transaction;
import com.project.library3.models.TransactionStatus;
import com.project.library3.services.PeopleService;
import com.project.library3.services.TransactionsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
import java.util.logging.Logger;

@Controller
@RequestMapping("/transaction")
public class TransactionController {
	private final PeopleService peopleService;
	private final TransactionsService transactionsService;

	@Autowired
	public TransactionController(PeopleService peopleService, TransactionsService transactionsService) {
		this.peopleService = peopleService;
		this.transactionsService = transactionsService;
	}

//	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public String sendTransaction(Model model, @ModelAttribute("transaction") Transaction transaction) {
		Logger.getGlobal().info(transaction.toString());
		if (peopleService.findOne(transaction.getDebtor().getId()).isPresent()) {
			Person debtor = peopleService.findOne(transaction.getDebtor().getId()).get();
			transaction.setReceivingAccount(12345678);
			transaction.setReceivingBank(1);
			transaction.setAmount(debtor.getFine());
			transaction.setCurrency(Currency.BYN);
			transaction.setStatus(TransactionStatus.PENDING);
			debtor.setFine(0.0);
//			peopleService.update(debtor.getId(), debtor);
			Logger.getGlobal().info(transaction.toString());
			transaction = transactionsService.save(transaction);			// сохраняем транзакцию в бд со статусом PENDING и получаем id


			RestClient restClient = RestClient.create("http://localhost:7070/api/transaction/pay");
			return restClient.post().contentType(MediaType.APPLICATION_JSON).body(transaction).retrieve().body(String.class);


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
		}
		else {
			model.addAttribute("id", transaction.getDebtor());
			return "people/incorrect_id";
		}
	}

//	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//	@ResponseBody
//	public Transaction sendTransaction(Model model, @ModelAttribute("transaction") Transaction transaction) {
//		return transaction;
//	}

//	@PostMapping("/redirection")
//	public ModelAndView redirection() {
//
//	}

	@PostMapping("/callback")
	public String getResult(@RequestBody Transaction transaction) {
		return "";
	}
}
