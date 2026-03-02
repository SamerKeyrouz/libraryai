package com.samer.libraryai.controller.models;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Builder
@Value
@Jacksonized
public class CreateBookRequest {
    @NotNull(message="Title is required")
    String title;

    @NotNull(message="Author is required")
    String author;

    @NotNull(message="Isbn is required")
    String isbn;

}
