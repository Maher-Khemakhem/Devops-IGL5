package tn.esprit.services.enrollment.service;

import java.util.List;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.services.enrollment.entity.EnrollmentView;
import tn.esprit.services.enrollment.query.EnrollmentQueries.FindAllEnrollmentsQuery;
import tn.esprit.services.enrollment.query.EnrollmentQueries.FindEnrollmentByIdQuery;

/** Read side: only talks to the QueryGateway. */
@Service
public class EnrollmentQueryService {
    private final QueryGateway queryGateway;

    public EnrollmentQueryService(QueryGateway queryGateway) { this.queryGateway = queryGateway; }

    public List<EnrollmentView> findAll() {
        return queryGateway.query(new FindAllEnrollmentsQuery(),
                ResponseTypes.multipleInstancesOf(EnrollmentView.class)).join();
    }

    public EnrollmentView findById(String id) {
        return queryGateway.query(new FindEnrollmentByIdQuery(id),
                ResponseTypes.optionalInstanceOf(EnrollmentView.class)).join()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollment not found"));
    }
}
