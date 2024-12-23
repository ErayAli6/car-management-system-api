package org.fmi.plovdiv.carmanagement.repository;

import org.fmi.plovdiv.carmanagement.model.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GarageRepository extends JpaRepository<Garage, Long> {
}
