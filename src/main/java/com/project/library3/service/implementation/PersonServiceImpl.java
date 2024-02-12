package com.project.library3.service.implementation;

import com.project.library3.repository.PersonRepository;
import com.project.library3.service.PersonService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.project.library3.domain.Book;
import com.project.library3.domain.Person;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {
	private final PersonRepository personRepository;

	@Autowired
	public PersonServiceImpl(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Person> findAll() {
		return personRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Person> findOne(int id) {
		return personRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Person> findOne(String name) {
		return personRepository.findByName(name);
	}

	@Override
	public void save(Person person) {
		personRepository.save(person);
	}

	@Override
	public void update(int id, Person updatedPerson) {
		updatedPerson.setId(id);
		personRepository.save(updatedPerson);
	}

	@Override
	public void delete(int id) {
		personRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Book> showAssignedBooks(int id) {
		Optional<Person> person = personRepository.findById(id);
		if (person.isPresent()) {
			Hibernate.initialize(person.get().getBooks());
			return person.get().getBooks();
		}
		else return Collections.emptyList();
	}

	@Override
	public void writeOffFine(Person debtor, Double amount) {
		debtor.setFine(debtor.getFine() - amount);
	}
}
