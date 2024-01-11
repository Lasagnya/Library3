package com.project.library3.controllers;

import com.project.library3.models.Currency;
import com.project.library3.models.Transaction;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.project.library3.models.Person;
import com.project.library3.services.PeopleService;
import com.project.library3.util.PersonValidator;
import org.springframework.web.client.RestClient;

import java.util.logging.Logger;

@Controller
@RequestMapping("/people")
public class PeopleController {
	private final PersonValidator personValidator;
	private final PeopleService peopleService;

	@Autowired
	public PeopleController(PersonValidator personValidator, PeopleService peopleService) {
		this.personValidator = personValidator;
		this.peopleService = peopleService;
	}

	@GetMapping()
	public String index(Model model) {
		model.addAttribute("people", peopleService.findAll());
		return "people/index";
	}

	@GetMapping("/{id}")
	public String show(Model model, @PathVariable("id") int id) {
		if (peopleService.findOne(id).isPresent()) {
			model.addAttribute("person", peopleService.findOne(id).get());
			model.addAttribute("books", peopleService.showAssignedBooks(id));
			return "people/show";
		}
		else {
			model.addAttribute("id", id);
			return "people/incorrect_id";
		}
	}

	@GetMapping("/new")
	public String newPerson(Model model) {
		model.addAttribute("person", new Person());
		return "people/new";
	}

	@PostMapping()
	public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
		personValidator.validate(person, bindingResult);
		if(bindingResult.hasErrors())
			return "people/new";
		peopleService.save(person);
		return "redirect:/people";
	}

	@GetMapping("/{id}/edit")
	public String edit(@PathVariable("id") int id, Model model) {
		if (peopleService.findOne(id).isPresent()) {
			model.addAttribute("person", peopleService.findOne(id).get());
			return "people/edit";
		}
		else {
			model.addAttribute("id", id);
			return "people/incorrect_id";
		}
	}

	@PatchMapping("/{id}")
	public String update(@PathVariable("id") int id, @ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
		personValidator.validate(person, bindingResult);
		if(bindingResult.hasErrors())
			return "people/edit";
		peopleService.update(id, person);
		return "redirect:/people";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") int id) {
		peopleService.delete(id);
		return "redirect:/people";
	}

	@GetMapping("/{id}/pay")
	public String pay(Model model, @PathVariable("id") int id) {
		if (peopleService.findOne(id).isPresent()) {
			Person person = peopleService.findOne(id).get();
			model.addAttribute("person", person);
			model.addAttribute("transaction", new Transaction(person));
			return "people/payment";
		}
		else {
			model.addAttribute("id", id);
			return "people/incorrect_id";
		}
	}

//	@PostMapping(value = "/{id}/transaction", produces = MediaType.TEXT_HTML_VALUE)
//	@ResponseBody
//	public String sendTransaction(Model model, @ModelAttribute("transaction") Transaction transaction, @PathVariable int id) {
//		if (peopleService.findOne(id).isPresent()) {
//			transaction.setReceivingAccount(12345678);
//			transaction.setReceivingBank(1);
//			transaction.setAmount(peopleService.findOne(id).get().getFine());
//			transaction.setCurrency(Currency.BYN);
//			Logger.getGlobal().info(transaction.toString());
//			RestClient restClient = RestClient.create("http://localhost:7070/api/transaction/pay");
//			String result = restClient.post().contentType(MediaType.APPLICATION_JSON).body(transaction).retrieve().body(String.class);
//			Logger.getGlobal().info(result);
//			return result;
//		}
//		else {
//			model.addAttribute("id", id);
//			return "people/incorrect_id";
//		}
//	}
}
