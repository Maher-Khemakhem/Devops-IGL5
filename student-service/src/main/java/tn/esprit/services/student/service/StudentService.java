package tn.esprit.services.student.service;

import java.util.List;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.services.student.client.DepartmentClient;
import tn.esprit.services.student.entity.Student;
import tn.esprit.services.student.repository.StudentRepository;

@Service
public class StudentService {
    private final StudentRepository repository;
    private final DepartmentClient departmentClient;

    public StudentService(StudentRepository repository, DepartmentClient departmentClient) {
        this.repository = repository;
        this.departmentClient = departmentClient;
    }

    public List<Student> findAll() { return repository.findAll(); }
    public Student findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
    }
    public Student save(Student student) {
        validateDepartment(student.getDepartmentId());
        student.setId(null);
        return repository.save(student);
    }
    public Student update(Long id, Student student) {
        if (!repository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        validateDepartment(student.getDepartmentId());
        student.setId(id);
        return repository.save(student);
    }
    public void delete(Long id) {
        if (!repository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        repository.deleteById(id);
    }

    private void validateDepartment(Long departmentId) {
        if (departmentId == null) return;
        try {
            departmentClient.findById(departmentId);
        } catch (FeignException exception) {
            if (exception.status() >= 400 && exception.status() < 500) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Department does not exist");
            }
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Academic service is unavailable");
        }
    }
}
