package tn.esprit.services.student.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.esprit.services.student.client.dto.DepartmentReference;

@FeignClient(name = "academic-service")
public interface DepartmentClient {
    @GetMapping("/departments/{id}")
    DepartmentReference findById(@PathVariable("id") Long id);
}
