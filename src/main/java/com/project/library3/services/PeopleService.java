package com.project.library3.services;

import com.project.library3.models.Book;
import com.project.library3.models.Person;

import java.util.List;
import java.util.Optional;

public interface PeopleService {
	List<Person> findAll();
	Optional<Person> findOne(int id);
	Optional<Person> findOne(String name);
	void save(Person person);
	void update(int id, Person updatedPerson);
	void delete(int id);
	List<Book> showAssignedBooks(int id);
	void writeOffFine(Person debtor, Double amount);
}
