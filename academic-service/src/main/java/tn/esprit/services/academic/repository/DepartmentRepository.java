package tn.esprit.services.academic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.services.academic.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> { }
