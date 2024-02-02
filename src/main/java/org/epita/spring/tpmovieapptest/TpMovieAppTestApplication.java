package org.epita.spring.tpmovieapptest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TpMovieAppTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TpMovieAppTestApplication.class, args);

		System.out.println("-----------------------------------");
		System.out.println("Spring App Launched !");
		System.out.println("-----------------------------------");
	}

}
