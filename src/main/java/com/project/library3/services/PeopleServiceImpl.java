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
public class PeopleServiceImpl implements PeopleService {
	private final PeopleRepository peopleRepository;

	@Autowired
	public PeopleServiceImpl(PeopleRepository peopleRepository) {
		this.peopleRepository = peopleRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Person> findAll() {
		return peopleRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Person> findOne(int id) {
		return peopleRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Person> findOne(String name) {
		return peopleRepository.findByName(name);
	}

	@Override
	public void save(Person person) {
		peopleRepository.save(person);
	}

	@Override
	public void update(int id, Person updatedPerson) {
		updatedPerson.setId(id);
		peopleRepository.save(updatedPerson);
	}

	@Override
	public void delete(int id) {
		peopleRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Book> showAssignedBooks(int id) {
		Optional<Person> person = peopleRepository.findById(id);
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
