package tn.esprit.services.enrollment.aggregate;

import java.time.LocalDate;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.services.enrollment.command.EnrollmentCommands.CreateEnrollmentCommand;
import tn.esprit.services.enrollment.command.EnrollmentCommands.DeleteEnrollmentCommand;
import tn.esprit.services.enrollment.command.EnrollmentCommands.UpdateEnrollmentCommand;
import tn.esprit.services.enrollment.entity.Status;
import tn.esprit.services.enrollment.event.EnrollmentEvents.EnrollmentCreatedEvent;
import tn.esprit.services.enrollment.event.EnrollmentEvents.EnrollmentDeletedEvent;
import tn.esprit.services.enrollment.event.EnrollmentEvents.EnrollmentUpdatedEvent;

class EnrollmentAggregateTest {
    private static final LocalDate DATE = LocalDate.of(2026, 11, 17);
    private FixtureConfiguration<EnrollmentAggregate> fixture;

    @BeforeEach
    void setUp() { fixture = new AggregateTestFixture<>(EnrollmentAggregate.class); }

    @Test
    void createEmitsCreatedEvent() {
        fixture.givenNoPriorActivity()
                .when(new CreateEnrollmentCommand("e1", DATE, 1L, 2L))
                .expectEvents(new EnrollmentCreatedEvent("e1", DATE, Status.ACTIVE, 1L, 2L));
    }

    @Test
    void createWithoutStudentFails() {
        fixture.givenNoPriorActivity()
                .when(new CreateEnrollmentCommand("e1", DATE, null, 2L))
                .expectException(IllegalArgumentException.class);
    }

    @Test
    void updateEmitsUpdatedEvent() {
        fixture.given(new EnrollmentCreatedEvent("e1", DATE, Status.ACTIVE, 1L, 2L))
                .when(new UpdateEnrollmentCommand("e1", DATE, 15.0, Status.COMPLETED, 1L, 2L))
                .expectEvents(new EnrollmentUpdatedEvent("e1", DATE, 15.0, Status.COMPLETED, 1L, 2L));
    }

    @Test
    void invalidGradeIsRejected() {
        fixture.given(new EnrollmentCreatedEvent("e1", DATE, Status.ACTIVE, 1L, 2L))
                .when(new UpdateEnrollmentCommand("e1", DATE, 25.0, Status.COMPLETED, 1L, 2L))
                .expectException(IllegalArgumentException.class);
    }

    @Test
    void deleteMarksAggregateDeleted() {
        fixture.given(new EnrollmentCreatedEvent("e1", DATE, Status.ACTIVE, 1L, 2L))
                .when(new DeleteEnrollmentCommand("e1"))
                .expectEvents(new EnrollmentDeletedEvent("e1"))
                .expectMarkedDeleted();
    }
}
