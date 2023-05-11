package ru.practicum.shareit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static ru.practicum.shareit.otherFunction.AddvansedFunctions.stringToGreenColor;

@SpringBootApplication
@Slf4j
public class ShareItApp {

	public static void main(String[] args) {
		SpringApplication.run(ShareItApp.class, args);
		log.info(stringToGreenColor("Program started"));
	}

}
