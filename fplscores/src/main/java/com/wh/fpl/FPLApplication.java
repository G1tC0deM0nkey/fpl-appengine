package com.wh.fpl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class FPLApplication {

    public static void main(String [] args) {
        SpringApplication.run(FPLApplication.class, args);
    }

}
