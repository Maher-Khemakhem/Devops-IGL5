package tn.esprit.services.enrollment.service;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.services.enrollment.client.CourseClient;
import tn.esprit.services.enrollment.client.StudentClient;
import tn.esprit.services.enrollment.client.dto.CourseReference;
import tn.esprit.services.enrollment.client.dto.StudentReference;

/**
 * Resilient wrappers around the remote calls.
 * Order of aspects: Retry( CircuitBreaker( RateLimiter( call ) ) ); the fallback is attached to the
 * outermost aspect (Retry) so it is only used once retries are exhausted or the circuit is open.
 */
@Service
public class RemoteLookupService {
    private static final Logger log = LoggerFactory.getLogger(RemoteLookupService.class);

    private final StudentClient studentClient;
    private final CourseClient courseClient;

    public RemoteLookupService(StudentClient studentClient, CourseClient courseClient) {
        this.studentClient = studentClient;
        this.courseClient = courseClient;
    }

    @Retry(name = "studentService", fallbackMethod = "studentFallback")
    @CircuitBreaker(name = "studentService")
    @RateLimiter(name = "studentService")
    public void verifyStudent(Long id) {
        try {
            StudentReference ref = studentClient.findById(id);
            if (ref == null || !id.equals(ref.id())) {
                throw new InvalidRemoteResponseException("Student service returned an invalid response for id " + id);
            }
        } catch (FeignException e) {
            throw translate("Student", e);
        }
    }

    @Retry(name = "courseService", fallbackMethod = "courseFallback")
    @CircuitBreaker(name = "courseService")
    @RateLimiter(name = "courseService")
    public void verifyCourse(Long id) {
        try {
            CourseReference ref = courseClient.findById(id);
            if (ref == null || !id.equals(ref.id())) {
                throw new InvalidRemoteResponseException("Academic service returned an invalid response for id " + id);
            }
        } catch (FeignException e) {
            throw translate("Course", e);
        }
    }

    // 4xx = business answer (not a failure): do not retry, do not count in the circuit breaker
    private RuntimeException translate(String resource, FeignException e) {
        if (e.status() >= 400 && e.status() < 500) {
            return new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, resource + " does not exist");
        }
        return e;
    }

    void studentFallback(Long id, Throwable t) { fallback("Student", id, t); }
    void courseFallback(Long id, Throwable t) { fallback("Course", id, t); }

    private void fallback(String resource, Long id, Throwable t) {
        if (t instanceof ResponseStatusException rse) throw rse;
        log.warn("Fallback for {} {} : {}", resource, id, t.toString());
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, resource + " service is unavailable, please retry later");
    }

    public static class InvalidRemoteResponseException extends RuntimeException {
        public InvalidRemoteResponseException(String message) { super(message); }
    }
}
