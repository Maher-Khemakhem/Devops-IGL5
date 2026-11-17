package tn.esprit.services.enrollment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.services.enrollment.entity.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> { }
