package com.samer.libraryai.repository;

import com.samer.libraryai.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("""
    SELECT b FROM Book b
    WHERE (:title IS NULL OR b.title ILIKE %:title%)
    AND (:author IS NULL OR b.author ILIKE %:author%)
    """)
    Page<Book> searchBooks(
            @Param("title") String title,
            @Param("author") String author,
            Pageable pageable
    );
}