package com.samer.libraryai.controller.models;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Builder
@Value
@Jacksonized
public class BookResponse {

    Long id;

    String title;

    String author;

    String isbn;

    boolean checkedOut;

    String borrowedBy;

    LocalDateTime borrowedAt;
}