package tn.esprit.services.enrollment.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.services.enrollment.entity.Enrollment;
import tn.esprit.services.enrollment.service.EnrollmentService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/enrollments")
public class EnrollmentController {
    private final EnrollmentService service;
    public EnrollmentController(EnrollmentService service) { this.service = service; }

    @GetMapping public List<Enrollment> findAll() { return service.findAll(); }
    @GetMapping("/{id}") public Enrollment findById(@PathVariable Long id) { return service.findById(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Enrollment create(@RequestBody Enrollment enrollment) { return service.create(enrollment); }
    @PutMapping("/{id}") public Enrollment update(@PathVariable Long id, @RequestBody Enrollment enrollment) { return service.update(id, enrollment); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.delete(id); }
}
