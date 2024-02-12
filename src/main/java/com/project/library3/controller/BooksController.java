package com.project.library3.controller;

import com.project.library3.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.project.library3.domain.Book;
import com.project.library3.service.BookService;

import java.util.logging.Logger;

@Controller
@RequestMapping("/books")
public class BooksController {
	private final BookService bookService;
	private final PersonService personService;

	@Autowired
	public BooksController(BookService bookService, PersonService personService) {
		this.bookService = bookService;
		this.personService = personService;
	}

	@GetMapping()
	public String index(Model model,
						@RequestParam(value = "page", required = false) Integer page,
						@RequestParam(value = "books_per_page", required = false) Integer size,
						@RequestParam(value = "sort_by_year", required = false) Boolean sort) {
		if (page == null || size == null)
			model.addAttribute("books", bookService.findAll(sort));
		else model.addAttribute("books", bookService.findAll(page, size, sort));
		return "books/index";
	}

	@GetMapping("/{id}")
	public String show(Model model, @PathVariable("id") int id) {
		if (bookService.findOne(id).isPresent()) {
			model.addAttribute("book", bookService.findOne(id).get());
			if (bookService.showAssignedPerson(id) == null)
				model.addAttribute("people", personService.findAll());
			return "books/show";
		}
		else model.addAttribute("id", id);
		return "books/incorrect_id";
	}

	@GetMapping("/new")
	public String newBook(Model model) {
		model.addAttribute("book", new Book());
		return "books/new";
	}

	@PostMapping()
	public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
		if(bindingResult.hasErrors())
			return "books/new";
		bookService.save(book);
		return "redirect:/books";
	}

	@GetMapping("/{id}/edit")
	public String edit(@PathVariable("id") int id, Model model) {
		if (bookService.findOne(id).isPresent()) {
			model.addAttribute("book", bookService.findOne(id).get());
			return "books/edit";
		}
		else {
			model.addAttribute("id", id);
			return "books/incorrect_id";
		}
	}

	@PatchMapping("/{id}")
	public String update(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
		if(bindingResult.hasErrors())
			return "books/edit";
		bookService.update(id, book);
		return String.format("redirect:/books/%d", id);
	}

	@PatchMapping("/{id}/assign")
	public String assign(@PathVariable("id") int id, @ModelAttribute("book") Book book) {
		Logger.getGlobal().info(book.getExpiryDate().toString());
		bookService.assign(id, book);
		return String.format("redirect:/books/%d", id);
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") int id) {
		bookService.delete(id);
		return "redirect:/books";
	}

	@PatchMapping("/{id}/delete_assign")
	public String deleteAssign(@PathVariable("id") int id) {
		bookService.deleteAssign(id);
		return String.format("redirect:/books/%d", id);
	}

	@GetMapping("/search")
	public String searchPage() {
		return "books/search";
	}

	@PostMapping("/search")
	public String search(@RequestParam("request") String request, Model model) {
		model.addAttribute("books", bookService.findByTitleStartingWith(request));
		return "books/search";
	}
}
