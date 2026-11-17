package tn.esprit.services.enrollment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class Enrollment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEnrollment;
    private LocalDate enrollmentDate;
    private Double grade;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Long studentId;
    private Long courseId;

    public Long getIdEnrollment() { return idEnrollment; }
    public void setIdEnrollment(Long idEnrollment) { this.idEnrollment = idEnrollment; }
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    public Double getGrade() { return grade; }
    public void setGrade(Double grade) { this.grade = grade; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
}
