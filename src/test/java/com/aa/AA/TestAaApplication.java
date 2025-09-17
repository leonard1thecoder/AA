package com.aa.AA;

import org.springframework.boot.SpringApplication;

public class TestAaApplication {

	public static void main(String[] args) {
		SpringApplication.from(AaApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
