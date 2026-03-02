package com.samer.libraryai.controller.models;

import lombok.Data;

@Data
public class SearchBookRequest {

    private String title;
    private String author;

    private int page = 0;
    private int size = 5;
    private String sortBy = "title";
}