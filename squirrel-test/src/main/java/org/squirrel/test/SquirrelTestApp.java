package org.squirrel.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author weicong
 * @time   2020年9月14日
 * @version 1.0
 */
@SpringBootApplication
public class SquirrelTestApp {

	public static void main(String[] args) throws JsonProcessingException {
		SpringApplication.run(SquirrelTestApp.class, args);
	}
}
