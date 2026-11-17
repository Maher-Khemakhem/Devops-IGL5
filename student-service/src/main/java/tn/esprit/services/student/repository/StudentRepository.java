package tn.esprit.services.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.services.student.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> { }
