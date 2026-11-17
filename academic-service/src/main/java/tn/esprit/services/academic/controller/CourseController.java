package tn.esprit.services.academic.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.services.academic.entity.Course;
import tn.esprit.services.academic.repository.CourseRepository;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/courses")
public class CourseController {
    private final CourseRepository repository;
    public CourseController(CourseRepository repository) { this.repository = repository; }

    @GetMapping public List<Course> findAll() { return repository.findAll(); }
    @GetMapping("/{id}") public Course findById(@PathVariable Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found")); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Course create(@RequestBody Course item) { item.setId(null); return repository.save(item); }
    @PutMapping("/{id}") public Course update(@PathVariable Long id, @RequestBody Course item) {
        if (!repository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        item.setId(id); return repository.save(item);
    }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) {
        if (!repository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        repository.deleteById(id);
    }
}
