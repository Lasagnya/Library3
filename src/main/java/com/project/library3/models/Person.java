package com.project.library3.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "person")
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Size(min = 3, max = 50, message = "Name should be between 3 and 50 characters")
	@Column(name = "name")
	private String name;

	@Min(value = 1800, message = "Birth should be greater than 1800")
	@Max(value = 3000, message = "Birth should be less than 3000")
	@Column(name = "birth")
	private int birth;

	@OneToMany(mappedBy = "owner")
	private List<Book> books;

	public Person() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBirth() {
		return birth;
	}

	public void setBirth(int age) {
		this.birth = age;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
}
