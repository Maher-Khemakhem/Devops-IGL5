package tn.esprit.services.enrollment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

/** Read model (query side), maintained by the projection from the events. */
@Entity
@Table(name = "enrollment_view")
public class EnrollmentView {
    @Id
    private String idEnrollment;
    private LocalDate enrollmentDate;
    private Double grade;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Long studentId;
    private Long courseId;

    public String getIdEnrollment() { return idEnrollment; }
    public void setIdEnrollment(String idEnrollment) { this.idEnrollment = idEnrollment; }
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
