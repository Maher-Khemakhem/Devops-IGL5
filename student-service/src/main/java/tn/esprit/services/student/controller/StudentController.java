package tn.esprit.services.student.controller;

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
import tn.esprit.services.student.entity.Student;
import tn.esprit.services.student.service.StudentService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/students")
public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) { this.service = service; }

    @GetMapping public List<Student> findAll() { return service.findAll(); }
    @GetMapping("/{id}") public Student findById(@PathVariable Long id) { return service.findById(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Student create(@RequestBody Student student) { return service.save(student); }
    @PutMapping("/{id}") public Student update(@PathVariable Long id, @RequestBody Student student) { return service.update(id, student); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.delete(id); }
}
