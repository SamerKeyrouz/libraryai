package com.samer.libraryai.service;


import com.samer.libraryai.controller.models.BookResponse;
import com.samer.libraryai.controller.models.CreateBookRequest;
import com.samer.libraryai.controller.models.UpdateBookRequest;
import com.samer.libraryai.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book toEntity(CreateBookRequest bookRequest);

    BookResponse toResponse(Book book);

   // Book toEntity(UpdateBookRequest bookRequest, Long id);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "checkedOut", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(UpdateBookRequest request, @MappingTarget Book book);


}
