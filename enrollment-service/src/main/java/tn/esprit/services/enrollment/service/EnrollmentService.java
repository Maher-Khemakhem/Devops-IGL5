package tn.esprit.services.enrollment.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.services.enrollment.entity.Enrollment;
import tn.esprit.services.enrollment.repository.EnrollmentRepository;

@Service
public class EnrollmentService {
    private final EnrollmentRepository repository;
    private final RestClient studentClient;
    private final RestClient academicClient;

    public EnrollmentService(EnrollmentRepository repository, RestClient.Builder builder,
            @Value("${clients.student-service-url}") String studentServiceUrl,
            @Value("${clients.academic-service-url}") String academicServiceUrl) {
        this.repository = repository;
        this.studentClient = builder.baseUrl(studentServiceUrl).build();
        this.academicClient = builder.baseUrl(academicServiceUrl).build();
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
        verifyExists(studentClient, "/students/" + enrollment.getStudentId(), "Student");
        verifyExists(academicClient, "/courses/" + enrollment.getCourseId(), "Course");
    }

    private void verifyExists(RestClient client, String path, String resource) {
        try {
            client.get().uri(path).retrieve().toBodilessEntity();
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().is4xxClientError()) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, resource + " does not exist");
            }
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, resource + " service is unavailable");
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, resource + " service is unavailable");
        }
    }
}
