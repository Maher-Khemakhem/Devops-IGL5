package tn.esprit.services.academic.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.services.academic.entity.Department;
import tn.esprit.services.academic.repository.DepartmentRepository;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/departments")
public class DepartmentController {
    private final DepartmentRepository repository;
    public DepartmentController(DepartmentRepository repository) { this.repository = repository; }

    @GetMapping public List<Department> findAll() { return repository.findAll(); }
    @GetMapping("/{id}") public Department findById(@PathVariable Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found")); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Department create(@RequestBody Department item) { item.setId(null); return repository.save(item); }
    @PutMapping("/{id}") public Department update(@PathVariable Long id, @RequestBody Department item) {
        if (!repository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found");
        item.setId(id); return repository.save(item);
    }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) {
        if (!repository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found");
        repository.deleteById(id);
    }
}
