package com.project.library3.service.implementation;

import com.project.library3.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.project.library3.domain.Book;
import com.project.library3.domain.Person;
import com.project.library3.repository.BookRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookServiceImpl implements BookService {
	private final BookRepository bookRepository;

	@Autowired
	public BookServiceImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Book> findAll(Boolean sort) {
		if (sort != null && sort)
			return bookRepository.findAll(Sort.by("year"));
		else return bookRepository.findAll();
	}

	@Override
	public List<Book> findAll(Integer page, Integer size, Boolean sort) {
		if (sort != null && sort)
			return bookRepository.findAll(PageRequest.of(page, size, Sort.by("year"))).getContent();
		else return bookRepository.findAll(PageRequest.of(page, size)).getContent();
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<Book> findOne(int id) {
		return bookRepository.findById(id);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Book> findByTitleStartingWith(String start) {
		return bookRepository.findByTitleStartingWith(start);
	}

	@Override
	public void save(Book book) {
		bookRepository.save(book);
	}

	@Override
	public void update(int id, Book updatedBook) {
		Book bookToBeUpdated = findOne(id).get();
		updatedBook.setId(id);
		updatedBook.setOwner(bookToBeUpdated.getOwner());
		bookRepository.save(updatedBook);
	}

	@Override
	public void delete(int id) {
		bookRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	@Override
	public Person showAssignedPerson(int id) {
		return bookRepository.findById(id).map(Book::getOwner).orElse(null);
	}

	@Override
	public void assign(int id, Book assignedBook) {
		bookRepository.findById(id).ifPresent(book -> {
			book.setOwner(assignedBook.getOwner());
			book.setTakingDate(LocalDate.now());
			book.setExpiryDate(assignedBook.getExpiryDate());
		});
	}

	@Override
	public void deleteAssign(int id) {
		bookRepository.findById(id).ifPresent(book -> {
			book.setOwner(null);
			book.setTakingDate(null);
			book.setExpiryDate(null);
		});
	}
}

