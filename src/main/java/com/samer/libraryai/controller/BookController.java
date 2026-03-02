package com.samer.libraryai.controller;

import com.samer.libraryai.controller.models.BookResponse;
import com.samer.libraryai.controller.models.SearchBookRequest;
import com.samer.libraryai.controller.models.UpdateBookRequest;
import com.samer.libraryai.service.BookService;
import com.samer.libraryai.controller.models.CreateBookRequest;
import com.samer.libraryai.entity.Book;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<BookResponse> getAllBooks(){
        return bookService.getAllBooks();
    }

    @PostMapping
    public BookResponse createBook(@RequestBody @Valid CreateBookRequest book){
        return bookService.createBook(book);
    }

    @GetMapping("/{id}")
    public BookResponse getBookById(@PathVariable Long id){
        return bookService.getBookById(id);
    }

    @PutMapping("/{id}")
    public BookResponse updateBook(@PathVariable Long id, @RequestBody @Valid UpdateBookRequest updatedBook){
        return bookService.updateBook(id, updatedBook);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
    }


    @PutMapping("/{id}/checkout")
    public BookResponse checkoutBook(@PathVariable Long id){
        return bookService.checkoutBook(id);
    }

    @PutMapping("/{id}/checkin")  //borrow book
    public BookResponse checkinBook(@PathVariable Long id){
        return bookService.checkinBook(id);
    }


    @GetMapping("/search")
    public Page<BookResponse> searchBooks(@ModelAttribute SearchBookRequest searchBookRequest) {
        return bookService.searchBooks(searchBookRequest);
    }

}
