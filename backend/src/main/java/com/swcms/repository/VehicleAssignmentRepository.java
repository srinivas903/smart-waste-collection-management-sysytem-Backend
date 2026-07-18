package com.swcms.repository;

import com.swcms.model.VehicleAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleAssignmentRepository extends JpaRepository<VehicleAssignment, Long> {
    List<VehicleAssignment> findByVehicleId(Long vehicleId);
    Optional<VehicleAssignment> findByPickupRequestId(Long pickupRequestId);
}
