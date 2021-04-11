package org.squirrel.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author weicong
 * @time   2020年9月14日
 * @version 1.0
 */
//@ComponentScan({"org.squirrel.*"})
@SpringBootApplication
public class App {

	public static void main(String[] args) throws JsonProcessingException {
		SpringApplication.run(App.class, args);
	}
}
