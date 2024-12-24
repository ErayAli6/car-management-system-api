package org.fmi.plovdiv.carmanagement.repository;

import org.fmi.plovdiv.carmanagement.model.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long>, JpaSpecificationExecutor<Maintenance> {

    long countByGarageIdAndScheduledDate(Long garageId, LocalDate scheduledDate);
}
