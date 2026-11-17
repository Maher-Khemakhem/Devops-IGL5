package tn.esprit.services.student.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.services.student.entity.Student;
import tn.esprit.services.student.repository.StudentRepository;

@Service
public class StudentService {
    private final StudentRepository repository;

    public StudentService(StudentRepository repository) { this.repository = repository; }

    public List<Student> findAll() { return repository.findAll(); }
    public Student findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
    }
    public Student save(Student student) { student.setId(null); return repository.save(student); }
    public Student update(Long id, Student student) {
        if (!repository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        student.setId(id);
        return repository.save(student);
    }
    public void delete(Long id) {
        if (!repository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        repository.deleteById(id);
    }
}
