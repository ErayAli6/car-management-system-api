package org.fmi.plovdiv.carmanagement.repository;

import org.fmi.plovdiv.carmanagement.model.Garage;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class GarageSpecification {

    public static Specification<Garage> hasCity(String city) {
        return (root, query, criteriaBuilder) ->
                city == null ? criteriaBuilder.conjunction() : criteriaBuilder.like(criteriaBuilder.lower(root.get("city")), "%" + city.toLowerCase() + "%");
    }
}
