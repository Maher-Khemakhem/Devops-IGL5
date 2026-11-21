package tn.esprit.services.enrollment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.esprit.services.enrollment.client.dto.StudentReference;

@FeignClient(name = "student-service")
public interface StudentClient {
    @GetMapping("/students/{id}")
    StudentReference findById(@PathVariable("id") Long id);
}
