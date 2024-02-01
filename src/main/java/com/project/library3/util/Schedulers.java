package com.project.library3.util;

import com.project.library3.models.Person;
import com.project.library3.services.PeopleService;
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
	private final PeopleService peopleService;
	private final Environment environment;

	public Schedulers(PeopleService peopleService, Environment environment) {
		this.peopleService = peopleService;
		this.environment = environment;
	}

	@Scheduled(fixedRate = 86_400_000)	// one day
//	@Scheduled(fixedRate = 1000)	// for test
	@Async
	@Transactional
	public void imposeFine() {
		List<Person> people = peopleService.findAll();
		for (Person person : people) {
			long amount = person.getBooks().stream().filter(book -> book.getExpiryDate().isEqual(LocalDate.now().minusDays(1))).count();
			person.setFine(person.getFine() + Double.parseDouble(Objects.requireNonNull(environment.getProperty("booking.fine"))) * amount);
		}
	}
}
