package org.fmi.plovdiv.carmanagement.repository;

import org.fmi.plovdiv.carmanagement.model.Maintenance;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MaintenanceSpecification {

    public static Specification<Maintenance> hasCarId(Long carId) {
        return (root, query, criteriaBuilder) ->
                carId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("car").get("id"), carId);
    }

    public static Specification<Maintenance> hasGarageId(Long garageId) {
        return (root, query, criteriaBuilder) ->
                garageId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("garage").get("id"), garageId);
    }

    public static Specification<Maintenance> scheduledBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return criteriaBuilder.conjunction();
            } else if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("scheduledDate"), startDate, endDate);
            } else if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("scheduledDate"), startDate);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("scheduledDate"), endDate);
            }
        };
    }
}
