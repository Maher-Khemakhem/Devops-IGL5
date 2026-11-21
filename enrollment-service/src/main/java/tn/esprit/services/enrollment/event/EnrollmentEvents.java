package tn.esprit.services.enrollment.event;

import java.time.LocalDate;
import tn.esprit.services.enrollment.entity.Status;

/** Domain events: facts stored in Axon Server (event store). */
public final class EnrollmentEvents {
    private EnrollmentEvents() { }

    public record EnrollmentCreatedEvent(
            String enrollmentId,
            LocalDate enrollmentDate,
            Status status,
            Long studentId,
            Long courseId) { }

    public record EnrollmentUpdatedEvent(
            String enrollmentId,
            LocalDate enrollmentDate,
            Double grade,
            Status status,
            Long studentId,
            Long courseId) { }

    public record EnrollmentDeletedEvent(String enrollmentId) { }
}
