package com.compras_service;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
//@EnableScheduling
public class ComprasServiceApplication {

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Bogota"));
        System.out.println("Zona horaria configurada: " + TimeZone.getDefault().getID());
    }

	public static void main(String[] args) {
		SpringApplication.run(ComprasServiceApplication.class, args);
	}

}
