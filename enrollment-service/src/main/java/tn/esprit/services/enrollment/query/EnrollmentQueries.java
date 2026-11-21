package tn.esprit.services.enrollment.query;

public final class EnrollmentQueries {
    private EnrollmentQueries() { }

    public record FindAllEnrollmentsQuery() { }

    public record FindEnrollmentByIdQuery(String enrollmentId) { }
}
