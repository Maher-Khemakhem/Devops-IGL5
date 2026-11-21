package tn.esprit.services.student.service;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.services.student.client.DepartmentClient;
import tn.esprit.services.student.client.dto.DepartmentReference;

/** Retry( CircuitBreaker( RateLimiter( call ) ) ) with the fallback on the outermost aspect. */
@Service
public class DepartmentLookupService {
    private static final Logger log = LoggerFactory.getLogger(DepartmentLookupService.class);

    private final DepartmentClient departmentClient;

    public DepartmentLookupService(DepartmentClient departmentClient) {
        this.departmentClient = departmentClient;
    }

    @Retry(name = "academicService", fallbackMethod = "departmentFallback")
    @CircuitBreaker(name = "academicService")
    @RateLimiter(name = "academicService")
    public void verifyDepartment(Long id) {
        try {
            DepartmentReference ref = departmentClient.findById(id);
            if (ref == null || !id.equals(ref.id())) {
                throw new InvalidRemoteResponseException("Academic service returned an invalid response for id " + id);
            }
        } catch (FeignException e) {
            if (e.status() >= 400 && e.status() < 500) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Department does not exist");
            }
            throw e;
        }
    }

    void departmentFallback(Long id, Throwable t) {
        if (t instanceof ResponseStatusException rse) throw rse;
        log.warn("Fallback for department {} : {}", id, t.toString());
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Academic service is unavailable, please retry later");
    }

    public static class InvalidRemoteResponseException extends RuntimeException {
        public InvalidRemoteResponseException(String message) { super(message); }
    }
}
