package com.project.library3.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.project.library3.models.Book;
import com.project.library3.models.Person;
import com.project.library3.repositories.PeopleRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PeopleService {
	private final PeopleRepository peopleRepository;

	@Autowired
	public PeopleService(PeopleRepository peopleRepository) {
		this.peopleRepository = peopleRepository;
	}

	@Transactional(readOnly = true)
	public List<Person> findAll() {
		return peopleRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Person> findOne(int id) {
		return peopleRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Optional<Person> findOne(String name) {
		return peopleRepository.findByName(name);
	}

	public void save(Person person) {
		peopleRepository.save(person);
	}

	public void update(int id, Person updatedPerson) {
		updatedPerson.setId(id);
		peopleRepository.save(updatedPerson);
	}

	public void delete(int id) {
		peopleRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<Book> showAssignedBooks(int id) {
		Optional<Person> person = peopleRepository.findById(id);
		if (person.isPresent()) {
			Hibernate.initialize(person.get().getBooks());
			return person.get().getBooks();
		}
		else return Collections.emptyList();
	}
}
