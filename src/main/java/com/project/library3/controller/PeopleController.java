package com.project.library3.controller;

import com.project.library3.domain.Transaction;
import com.project.library3.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.project.library3.domain.Person;
import com.project.library3.util.PersonValidator;

@Controller
@RequestMapping("/people")
public class PeopleController {
	private final PersonValidator personValidator;
	private final PersonService personService;

	@Autowired
	public PeopleController(PersonValidator personValidator, PersonService personService) {
		this.personValidator = personValidator;
		this.personService = personService;
	}

	@GetMapping()
	public String index(Model model) {
		model.addAttribute("people", personService.findAll());
		return "people/index";
	}

	@GetMapping("/{id}")
	public String show(Model model, @PathVariable("id") int id) {
		if (personService.findOne(id).isPresent()) {
			model.addAttribute("person", personService.findOne(id).get());
			model.addAttribute("books", personService.showAssignedBooks(id));
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
		personService.save(person);
		return "redirect:/people";
	}

	@GetMapping("/{id}/edit")
	public String edit(@PathVariable("id") int id, Model model) {
		if (personService.findOne(id).isPresent()) {
			model.addAttribute("person", personService.findOne(id).get());
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
		personService.update(id, person);
		return "redirect:/people";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") int id) {
		personService.delete(id);
		return "redirect:/people";
	}

	@GetMapping("/{id}/pay")
	public String pay(Model model, @PathVariable("id") int id) {
		if (personService.findOne(id).isPresent()) {
			Person person = personService.findOne(id).get();
			model.addAttribute("person", person);
			model.addAttribute("transaction", new Transaction(person));
			return "people/payment";
		}
		else {
			model.addAttribute("id", id);
			return "people/incorrect_id";
		}
	}
}
