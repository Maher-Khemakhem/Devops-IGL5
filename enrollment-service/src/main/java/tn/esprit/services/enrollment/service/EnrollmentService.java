package tn.esprit.services.enrollment.service;

import java.util.List;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.services.enrollment.client.CourseClient;
import tn.esprit.services.enrollment.client.StudentClient;
import tn.esprit.services.enrollment.entity.Enrollment;
import tn.esprit.services.enrollment.repository.EnrollmentRepository;

@Service
public class EnrollmentService {
    private final EnrollmentRepository repository;
    private final StudentClient studentClient;
    private final CourseClient courseClient;

    public EnrollmentService(EnrollmentRepository repository, StudentClient studentClient, CourseClient courseClient) {
        this.repository = repository;
        this.studentClient = studentClient;
        this.courseClient = courseClient;
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
        verifyStudentExists(enrollment.getStudentId());
        verifyCourseExists(enrollment.getCourseId());
    }

    private void verifyStudentExists(Long id) {
        try {
            studentClient.findById(id);
        } catch (FeignException exception) {
            throw translateRemoteError("Student", exception);
        }
    }

    private void verifyCourseExists(Long id) {
        try {
            courseClient.findById(id);
        } catch (FeignException exception) {
            throw translateRemoteError("Course", exception);
        }
    }

    private ResponseStatusException translateRemoteError(String resource, FeignException exception) {
        if (exception.status() >= 400 && exception.status() < 500) {
            return new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, resource + " does not exist");
        }
        return new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, resource + " service is unavailable");
    }
}
