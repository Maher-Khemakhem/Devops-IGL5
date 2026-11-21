package tn.esprit.services.enrollment.command;

import java.time.LocalDate;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import tn.esprit.services.enrollment.entity.Status;

/** CQRS commands: intentions sent to the aggregate. */
public final class EnrollmentCommands {
    private EnrollmentCommands() { }

    public record CreateEnrollmentCommand(
            @TargetAggregateIdentifier String enrollmentId,
            LocalDate enrollmentDate,
            Long studentId,
            Long courseId) { }

    public record UpdateEnrollmentCommand(
            @TargetAggregateIdentifier String enrollmentId,
            LocalDate enrollmentDate,
            Double grade,
            Status status,
            Long studentId,
            Long courseId) { }

    public record DeleteEnrollmentCommand(@TargetAggregateIdentifier String enrollmentId) { }
}
