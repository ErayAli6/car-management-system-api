package org.fmi.plovdiv.carmanagement.repository;

import org.fmi.plovdiv.carmanagement.model.Car;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CarSpecification {

    public static Specification<Car> hasMake(String make) {
        return (root, query, criteriaBuilder) ->
                make == null ? criteriaBuilder.conjunction() : criteriaBuilder.like(criteriaBuilder.lower(root.get("make")), "%" + make.toLowerCase() + "%");
    }

    public static Specification<Car> belongsToGarage(Long garageId) {
        return (root, query, criteriaBuilder) ->
                garageId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.join("garages").get("id"), garageId);
    }

    public static Specification<Car> productionYearBetween(Integer fromYear, Integer toYear) {
        return (root, query, criteriaBuilder) -> {
            if (fromYear == null && toYear == null) {
                return criteriaBuilder.conjunction();
            } else if (fromYear != null && toYear != null) {
                return criteriaBuilder.between(root.get("productionYear"), fromYear, toYear);
            } else if (fromYear != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("productionYear"), fromYear);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("productionYear"), toYear);
            }
        };
    }
}
