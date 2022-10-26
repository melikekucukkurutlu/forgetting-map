package com.melike.interview.forgettingmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ForgettingMapApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForgettingMapApplication.class, args);

		ForgettingMap<Integer, String> forgettingMap = new ForgettingMap<>(2);
		forgettingMap.add(1, "first association");
		forgettingMap.add(2, "second association");
		System.out.println(forgettingMap);
		forgettingMap.find(1);
		System.out.println(forgettingMap);
		forgettingMap.add(3, "third association");
		System.out.println(forgettingMap);
	}

}
