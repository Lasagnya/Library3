package com.project.library3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.project.library3.models.Book;
import com.project.library3.models.Person;
import com.project.library3.repositories.BooksRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BooksServiceImpl implements BooksService {
	private final BooksRepository booksRepository;

	@Autowired
	public BooksServiceImpl(BooksRepository booksRepository) {
		this.booksRepository = booksRepository;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Book> findAll(Boolean sort) {
		if (sort != null && sort)
			return booksRepository.findAll(Sort.by("year"));
		else return booksRepository.findAll();
	}

	@Override
	public List<Book> findAll(Integer page, Integer size, Boolean sort) {
		if (sort != null && sort)
			return booksRepository.findAll(PageRequest.of(page, size, Sort.by("year"))).getContent();
		else return booksRepository.findAll(PageRequest.of(page, size)).getContent();
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<Book> findOne(int id) {
		return booksRepository.findById(id);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Book> findByTitleStartingWith(String start) {
		return booksRepository.findByTitleStartingWith(start);
	}

	@Override
	public void save(Book book) {
		booksRepository.save(book);
	}

	@Override
	public void update(int id, Book updatedBook) {
		Book bookToBeUpdated = findOne(id).get();
		updatedBook.setId(id);
		updatedBook.setOwner(bookToBeUpdated.getOwner());
		booksRepository.save(updatedBook);
	}

	@Override
	public void delete(int id) {
		booksRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	@Override
	public Person showAssignedPerson(int id) {
		return booksRepository.findById(id).map(Book::getOwner).orElse(null);
	}

	@Override
	public void assign(int id, Book assignedBook) {
		booksRepository.findById(id).ifPresent(book -> {
			book.setOwner(assignedBook.getOwner());
			book.setTakingDate(LocalDate.now());
			book.setExpiryDate(assignedBook.getExpiryDate());
		});
	}

	@Override
	public void deleteAssign(int id) {
		booksRepository.findById(id).ifPresent(book -> {
			book.setOwner(null);
			book.setTakingDate(null);
			book.setExpiryDate(null);
		});
	}
}

