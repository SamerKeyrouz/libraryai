package com.samer.libraryai.service;


import com.samer.libraryai.controller.models.BookResponse;
import com.samer.libraryai.controller.models.CreateBookRequest;
import com.samer.libraryai.controller.models.SearchBookRequest;
import com.samer.libraryai.controller.models.UpdateBookRequest;
import com.samer.libraryai.entity.Book;
import com.samer.libraryai.exception.InvalidOperationException;
import com.samer.libraryai.exception.ResourceNotFoundException;
import com.samer.libraryai.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDateTime;

import java.util.List;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public List<BookResponse> getAllBooks(){
        return bookRepository.findAll().stream().map(bookMapper::toResponse).toList();
    }

    public BookResponse createBook(CreateBookRequest bookRequest){
        Book book = bookMapper.toEntity(bookRequest);
        book.setCheckedOut(false);
        return bookMapper.toResponse(bookRepository.save(book));
    }

    public BookResponse getBookById(Long id){
        Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        return bookMapper.toResponse(book);
    }

    public BookResponse updateBook(Long id, UpdateBookRequest updatedBookRequest){

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));


        //Book updatedBook = bookMapper.toEntity(updatedBookRequest, id);
        bookMapper.updateEntity(updatedBookRequest, existingBook);

        return bookMapper.toResponse(bookRepository.save(existingBook));
    }

    public void deleteBook(Long id){

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        bookRepository.delete(existingBook);
    }

    public BookResponse checkoutBook(Long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.isCheckedOut()) {
            throw new InvalidOperationException("Book is already checked out");
        }

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (!(authentication.getPrincipal() instanceof OAuth2User oauthUser)) {
            throw new RuntimeException("User not authenticated properly");
        }

        String email = oauthUser.getAttribute("email");

        book.setCheckedOut(true);
        book.setBorrowedBy(email);
        book.setBorrowedAt(LocalDateTime.now());

        return bookMapper.toResponse(bookRepository.save(book));
    }

    public BookResponse checkinBook(Long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (!book.isCheckedOut()) {
            throw new InvalidOperationException("Book is already checked in");
        }

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (!(authentication.getPrincipal() instanceof OAuth2User oauthUser)) {
            throw new RuntimeException("User not authenticated properly");
        }

        String email = oauthUser.getAttribute("email");

        // Only borrower can return
        if (!email.equals(book.getBorrowedBy())) {
            throw new InvalidOperationException("You cannot return a book you didn't borrow");
        }

        book.setCheckedOut(false);
        book.setBorrowedBy(null);
        book.setBorrowedAt(null);

        return bookMapper.toResponse(bookRepository.save(book));
    }

    public Page<BookResponse> searchBooks(SearchBookRequest searchBookRequest) {

        int page = Math.max(searchBookRequest.getPage(), 0);

        int size = searchBookRequest.getSize();
        if (size <= 0) size = 5;
        if (size > 50) size = 50;

        String sortBy = searchBookRequest.getSortBy();

        List<String> allowedSortFields = List.of(
                "id", "title", "author", "isbn", "checkedOut", "createdAt"
        );

        if (sortBy == null || sortBy.isBlank() || !allowedSortFields.contains(sortBy)) {
            sortBy = "title";
        }
        Pageable pageable = PageRequest.of(searchBookRequest.getPage(), searchBookRequest.getSize(), Sort.by(searchBookRequest.getSortBy()));

        String title = searchBookRequest.getTitle() != null && !searchBookRequest.getTitle().trim().isEmpty()? searchBookRequest.getTitle().toLowerCase() : null;
        String author = searchBookRequest.getAuthor() != null && !searchBookRequest.getAuthor().trim().isEmpty()? searchBookRequest.getAuthor().toLowerCase() : null;

        Page <Book> books = bookRepository.searchBooks(title,author,pageable);

        return books.map(bookMapper::toResponse);
    }


}
