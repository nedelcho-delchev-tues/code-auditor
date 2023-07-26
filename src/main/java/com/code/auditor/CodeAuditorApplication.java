package com.code.auditor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EntityScan(basePackages = {"com.code.auditor.domain"})
public class CodeAuditorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeAuditorApplication.class, args);
    }

}
