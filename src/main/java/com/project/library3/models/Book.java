package com.project.library3.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;

import java.time.temporal.ChronoUnit;
import java.util.Date;

@Entity
@Table(name = "book")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@ManyToOne
	@JoinColumn(name = "person_id", referencedColumnName = "id")
	private Person owner;

	@Size(min = 2, max = 100, message = "Title should be between 2 and 100 characters")
	@Column(name = "title")
	private String title;

	@Size(min = 3, max = 50, message = "Author should be between 3 and 50 characters")
	@Column(name = "author")
	private String author;

	@Max(value = 3000, message = "Year should be less than 3000")
	@Column(name = "year")
	private int year;

	@Column(name = "taking_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date takingTime;

	public Book() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}

	public Date getTakingTime() {
		return takingTime;
	}

	public void setTakingTime(Date takingTime) {
		this.takingTime = takingTime;
	}

	public boolean isExpired() {
		long diff = Math.abs(ChronoUnit.DAYS.between(new Date().toInstant(), getTakingTime().toInstant()));
		return diff > 10;
	}
}
