package com.project.library3.util;

import com.project.library3.models.Book;
import com.project.library3.models.Person;
import com.project.library3.services.PeopleService;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Service
public class Schedulers {
	private final PeopleService peopleService;
	private final Environment environment;

	public Schedulers(PeopleService peopleService, Environment environment) {
		this.peopleService = peopleService;
		this.environment = environment;
	}

	@Scheduled(fixedRate = 86_400_000)
	@Async
	@Transactional
	public void imposeFine() {
		List<Person> people = peopleService.findAll();
		for (Person person : people) {
			long amount = person.getBooks().stream().filter(Book::isExpired).count();
			person.setFine(Double.parseDouble(Objects.requireNonNull(environment.getProperty("booking.fine"))) * amount);
		}
	}
}
