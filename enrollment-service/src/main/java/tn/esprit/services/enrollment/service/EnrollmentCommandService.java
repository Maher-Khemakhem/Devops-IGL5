package tn.esprit.services.enrollment.service;

import java.util.UUID;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.services.enrollment.command.EnrollmentCommands.CreateEnrollmentCommand;
import tn.esprit.services.enrollment.command.EnrollmentCommands.DeleteEnrollmentCommand;
import tn.esprit.services.enrollment.command.EnrollmentCommands.UpdateEnrollmentCommand;
import tn.esprit.services.enrollment.entity.EnrollmentView;
import tn.esprit.services.enrollment.entity.Status;
import tn.esprit.services.enrollment.repository.EnrollmentViewRepository;

/** Write side: validates (local rules + remote references), then dispatches commands. */
@Service
public class EnrollmentCommandService {
    private final CommandGateway commandGateway;
    private final RemoteLookupService lookup;
    private final EnrollmentViewRepository views;

    public EnrollmentCommandService(CommandGateway commandGateway, RemoteLookupService lookup, EnrollmentViewRepository views) {
        this.commandGateway = commandGateway;
        this.lookup = lookup;
        this.views = views;
    }

    /** @return the id of the new enrollment */
    public String create(EnrollmentView request) {
        validateReferences(request);
        if (views.existsByStudentIdAndCourseIdAndStatus(request.getStudentId(), request.getCourseId(), Status.ACTIVE)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student is already enrolled in this course");
        }
        String id = UUID.randomUUID().toString();
        commandGateway.sendAndWait(new CreateEnrollmentCommand(id, request.getEnrollmentDate(),
                request.getStudentId(), request.getCourseId()));
        return id;
    }

    public void update(String id, EnrollmentView request) {
        ensureExists(id);
        validateReferences(request);
        if (request.getGrade() != null && (request.getGrade() < 0 || request.getGrade() > 20)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "grade must be between 0 and 20");
        }
        commandGateway.sendAndWait(new UpdateEnrollmentCommand(id, request.getEnrollmentDate(), request.getGrade(),
                request.getStatus(), request.getStudentId(), request.getCourseId()));
    }

    public void delete(String id) {
        ensureExists(id);
        commandGateway.sendAndWait(new DeleteEnrollmentCommand(id));
    }

    private void ensureExists(String id) {
        if (!views.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollment not found");
    }

    private void validateReferences(EnrollmentView request) {
        if (request.getStudentId() == null || request.getCourseId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "studentId and courseId are required");
        }
        lookup.verifyStudent(request.getStudentId());
        lookup.verifyCourse(request.getCourseId());
    }
}
