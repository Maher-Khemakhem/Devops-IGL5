package tn.esprit.services.enrollment.aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;
import static org.axonframework.modelling.command.AggregateLifecycle.markDeleted;

import java.time.LocalDate;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import tn.esprit.services.enrollment.command.EnrollmentCommands.CreateEnrollmentCommand;
import tn.esprit.services.enrollment.command.EnrollmentCommands.DeleteEnrollmentCommand;
import tn.esprit.services.enrollment.command.EnrollmentCommands.UpdateEnrollmentCommand;
import tn.esprit.services.enrollment.entity.Status;
import tn.esprit.services.enrollment.event.EnrollmentEvents.EnrollmentCreatedEvent;
import tn.esprit.services.enrollment.event.EnrollmentEvents.EnrollmentDeletedEvent;
import tn.esprit.services.enrollment.event.EnrollmentEvents.EnrollmentUpdatedEvent;

/** Command side: business rules, rebuilt from its events (event sourcing). */
@Aggregate
public class EnrollmentAggregate {
    @AggregateIdentifier
    private String enrollmentId;
    private Status status;

    protected EnrollmentAggregate() { }

    @CommandHandler
    public EnrollmentAggregate(CreateEnrollmentCommand command) {
        if (command.studentId() == null || command.courseId() == null) {
            throw new IllegalArgumentException("studentId and courseId are required");
        }
        LocalDate date = command.enrollmentDate() != null ? command.enrollmentDate() : LocalDate.now();
        apply(new EnrollmentCreatedEvent(command.enrollmentId(), date, Status.ACTIVE,
                command.studentId(), command.courseId()));
    }

    @CommandHandler
    public void handle(UpdateEnrollmentCommand command) {
        if (command.studentId() == null || command.courseId() == null) {
            throw new IllegalArgumentException("studentId and courseId are required");
        }
        if (command.grade() != null && (command.grade() < 0 || command.grade() > 20)) {
            throw new IllegalArgumentException("grade must be between 0 and 20");
        }
        Status newStatus = command.status() != null ? command.status() : status;
        apply(new EnrollmentUpdatedEvent(enrollmentId, command.enrollmentDate(), command.grade(),
                newStatus, command.studentId(), command.courseId()));
    }

    @CommandHandler
    public void handle(DeleteEnrollmentCommand command) {
        apply(new EnrollmentDeletedEvent(enrollmentId));
    }

    @EventSourcingHandler
    public void on(EnrollmentCreatedEvent event) {
        this.enrollmentId = event.enrollmentId();
        this.status = event.status();
    }

    @EventSourcingHandler
    public void on(EnrollmentUpdatedEvent event) {
        this.status = event.status();
    }

    @EventSourcingHandler
    public void on(EnrollmentDeletedEvent event) {
        markDeleted();
    }
}
