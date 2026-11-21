package tn.esprit.services.enrollment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.services.enrollment.entity.EnrollmentView;
import tn.esprit.services.enrollment.entity.Status;

public interface EnrollmentViewRepository extends JpaRepository<EnrollmentView, String> {
    boolean existsByStudentIdAndCourseIdAndStatus(Long studentId, Long courseId, Status status);
}
