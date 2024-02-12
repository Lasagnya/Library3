package com.project.library3.util;

import com.project.library3.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.project.library3.domain.Person;

@Component
public class PersonValidator implements Validator {
	private final PersonService personService;

	@Autowired
	public PersonValidator(PersonService personService) {
		this.personService = personService;
	}

	@Override
	public boolean supports(Class<?> clazz) {	//для какой сущности валидатор предназначен
		return Person.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Person person = (Person) target;
		if (personService.findOne(person.getName()).isPresent())
			if (personService.findOne(person.getName()).get().getId() != person.getId())
				errors.rejectValue("name", "", "This name is already taken.");
	}
}
