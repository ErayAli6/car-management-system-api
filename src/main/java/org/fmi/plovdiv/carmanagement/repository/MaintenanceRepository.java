package org.fmi.plovdiv.carmanagement.repository;

import org.fmi.plovdiv.carmanagement.model.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
}