package com.project.library3.util;

import com.project.library3.domain.Person;
import com.project.library3.service.PersonService;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class Schedulers {
	private final PersonService personService;
	private final Environment environment;

	public Schedulers(PersonService personService, Environment environment) {
		this.personService = personService;
		this.environment = environment;
	}

	@Scheduled(fixedRate = 86_400_000)	// one day
//	@Scheduled(fixedRate = 1000)	// for test
	@Async
	@Transactional
	public void imposeFine() {
		List<Person> people = personService.findAll();
		for (Person person : people) {
			long amount = person.getBooks().stream().filter(book -> book.getExpiryDate().isEqual(LocalDate.now().minusDays(1))).count();
			person.setFine(person.getFine() + Double.parseDouble(Objects.requireNonNull(environment.getProperty("booking.fine"))) * amount);
		}
	}
}
