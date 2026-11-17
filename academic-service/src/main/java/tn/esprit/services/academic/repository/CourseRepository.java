package tn.esprit.services.academic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.services.academic.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> { }
