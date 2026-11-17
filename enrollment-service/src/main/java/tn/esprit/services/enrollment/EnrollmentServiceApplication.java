package tn.esprit.services.enrollment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "tn.esprit.services.enrollment")
public class EnrollmentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EnrollmentServiceApplication.class, args);
    }
}
