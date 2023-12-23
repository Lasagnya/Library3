package com.project.library3.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.project.library3.models.Person;
import com.project.library3.services.PeopleService;

@Component
public class PersonValidator implements Validator {
	private final PeopleService peopleService;

	@Autowired
	public PersonValidator(PeopleService peopleService) {
		this.peopleService = peopleService;
	}

	@Override
	public boolean supports(Class<?> clazz) {	//для какой сущности валидатор предназначен
		return Person.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Person person = (Person) target;
		if (peopleService.findOne(person.getName()).isPresent())
			if (peopleService.findOne(person.getName()).get().getId() != person.getId())
				errors.rejectValue("name", "", "This name is already taken.");
	}
}
