package tn.esprit.services.enrollment.controller;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.services.enrollment.entity.EnrollmentView;
import tn.esprit.services.enrollment.service.EnrollmentQueryService;

/** CQRS read API: GET only. */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/enrollments")
public class EnrollmentQueryController {
    private final EnrollmentQueryService queries;

    public EnrollmentQueryController(EnrollmentQueryService queries) { this.queries = queries; }

    @GetMapping public List<EnrollmentView> findAll() { return queries.findAll(); }
    @GetMapping("/{id}") public EnrollmentView findById(@PathVariable String id) { return queries.findById(id); }
}
