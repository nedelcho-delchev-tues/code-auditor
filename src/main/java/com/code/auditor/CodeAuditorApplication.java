package com.code.auditor;

import com.code.auditor.domain.User;
import com.code.auditor.enums.Role;
import com.code.auditor.services.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EntityScan(basePackages = {"com.code.auditor.domain"})
public class CodeAuditorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeAuditorApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(AuthenticationService service) {
        return args -> {
            User admin = new User("admin", "admin", "admin@codeauditor.com", "123456", Role.ADMIN);
            User student = new User("user", "user", "user@codeauditor.com", "123456", Role.STUDENT);

            System.out.println("Student token: " + service.register(student).getAccessToken());
            System.out.println("Admin token: " + service.register(admin).getAccessToken());
        };
    }
}
