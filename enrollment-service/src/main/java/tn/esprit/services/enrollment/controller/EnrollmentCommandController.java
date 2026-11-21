package tn.esprit.services.enrollment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.services.enrollment.entity.EnrollmentView;
import tn.esprit.services.enrollment.service.EnrollmentCommandService;
import tn.esprit.services.enrollment.service.EnrollmentQueryService;

/** CQRS write API: POST / PUT / DELETE. */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/enrollments")
public class EnrollmentCommandController {
    private final EnrollmentCommandService commands;
    private final EnrollmentQueryService queries;

    public EnrollmentCommandController(EnrollmentCommandService commands, EnrollmentQueryService queries) {
        this.commands = commands;
        this.queries = queries;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EnrollmentView create(@RequestBody EnrollmentView request) {
        return queries.findById(commands.create(request));
    }

    @PutMapping("/{id}")
    public EnrollmentView update(@PathVariable String id, @RequestBody EnrollmentView request) {
        commands.update(id, request);
        return queries.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) { commands.delete(id); }
}
