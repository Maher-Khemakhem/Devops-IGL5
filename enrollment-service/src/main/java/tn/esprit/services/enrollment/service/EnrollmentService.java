package tn.esprit.services.enrollment.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.services.enrollment.entity.Enrollment;
import tn.esprit.services.enrollment.repository.EnrollmentRepository;

@Service
public class EnrollmentService {
    private final EnrollmentRepository repository;
    private final RemoteLookupService lookup;

    public EnrollmentService(EnrollmentRepository repository, RemoteLookupService lookup) {
        this.repository = repository;
        this.lookup = lookup;
    }

    public List<Enrollment> findAll() { return repository.findAll(); }
    public Enrollment findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollment not found"));
    }
    public Enrollment create(Enrollment enrollment) {
        validateReferences(enrollment);
        enrollment.setIdEnrollment(null);
        return repository.save(enrollment);
    }
    public Enrollment update(Long id, Enrollment enrollment) {
        if (!repository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollment not found");
        validateReferences(enrollment);
        enrollment.setIdEnrollment(id);
        return repository.save(enrollment);
    }
    public void delete(Long id) {
        if (!repository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollment not found");
        repository.deleteById(id);
    }

    private void validateReferences(Enrollment enrollment) {
        if (enrollment.getStudentId() == null || enrollment.getCourseId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "studentId and courseId are required");
        }
        lookup.verifyStudent(enrollment.getStudentId());
        lookup.verifyCourse(enrollment.getCourseId());
    }

}
