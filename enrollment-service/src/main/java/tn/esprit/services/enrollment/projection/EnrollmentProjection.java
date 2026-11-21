package tn.esprit.services.enrollment.projection;

import java.util.List;
import java.util.Optional;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import tn.esprit.services.enrollment.entity.EnrollmentView;
import tn.esprit.services.enrollment.event.EnrollmentEvents.EnrollmentCreatedEvent;
import tn.esprit.services.enrollment.event.EnrollmentEvents.EnrollmentDeletedEvent;
import tn.esprit.services.enrollment.event.EnrollmentEvents.EnrollmentUpdatedEvent;
import tn.esprit.services.enrollment.query.EnrollmentQueries.FindAllEnrollmentsQuery;
import tn.esprit.services.enrollment.query.EnrollmentQueries.FindEnrollmentByIdQuery;
import tn.esprit.services.enrollment.repository.EnrollmentViewRepository;

/** Projector: events -> MySQL read model, and answers the queries. */
@Component
@ProcessingGroup("enrollment")
public class EnrollmentProjection {
    private final EnrollmentViewRepository repository;

    public EnrollmentProjection(EnrollmentViewRepository repository) { this.repository = repository; }

    @EventHandler
    public void on(EnrollmentCreatedEvent event) {
        EnrollmentView view = new EnrollmentView();
        view.setIdEnrollment(event.enrollmentId());
        view.setEnrollmentDate(event.enrollmentDate());
        view.setStatus(event.status());
        view.setStudentId(event.studentId());
        view.setCourseId(event.courseId());
        repository.save(view);
    }

    @EventHandler
    public void on(EnrollmentUpdatedEvent event) {
        repository.findById(event.enrollmentId()).ifPresent(view -> {
            view.setEnrollmentDate(event.enrollmentDate());
            view.setGrade(event.grade());
            view.setStatus(event.status());
            view.setStudentId(event.studentId());
            view.setCourseId(event.courseId());
            repository.save(view);
        });
    }

    @EventHandler
    public void on(EnrollmentDeletedEvent event) {
        repository.deleteById(event.enrollmentId());
    }

    @QueryHandler
    public List<EnrollmentView> handle(FindAllEnrollmentsQuery query) {
        return repository.findAll();
    }

    @QueryHandler
    public Optional<EnrollmentView> handle(FindEnrollmentByIdQuery query) {
        return repository.findById(query.enrollmentId());
    }
}
