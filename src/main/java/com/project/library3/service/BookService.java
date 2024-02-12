package com.project.library3.service;

import com.project.library3.domain.Book;
import com.project.library3.domain.Person;

import java.util.List;
import java.util.Optional;

public interface BookService {
	List<Book> findAll(Boolean sort);

	List<Book> findAll(Integer page, Integer size, Boolean sort);

	Optional<Book> findOne(int id);

	List<Book> findByTitleStartingWith(String start);

	void save(Book book);

	void update(int id, Book updatedBook);

	void delete(int id);

	Person showAssignedPerson(int id);

	void assign(int id, Book assignedBook);

	void deleteAssign(int id);
}
