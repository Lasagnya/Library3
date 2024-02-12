package com.project.library3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project.library3.domain.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
	List<Book> findByTitleStartingWith(String start);
}
