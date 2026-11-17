package tn.esprit.services.academic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "tn.esprit.services.academic")
public class AcademicServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AcademicServiceApplication.class, args);
    }
}
