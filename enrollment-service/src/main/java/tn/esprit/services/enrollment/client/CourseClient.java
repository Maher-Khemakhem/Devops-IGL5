package tn.esprit.services.enrollment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.esprit.services.enrollment.client.dto.CourseReference;

@FeignClient(name = "academic-service")
public interface CourseClient {
    @GetMapping("/courses/{id}")
    CourseReference findById(@PathVariable("id") Long id);
}
