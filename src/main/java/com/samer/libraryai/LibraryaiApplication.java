package com.samer.libraryai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LibraryaiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryaiApplication.class, args);
	}

}
