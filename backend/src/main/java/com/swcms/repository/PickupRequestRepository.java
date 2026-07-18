package com.swcms.repository;

import com.swcms.model.PickupRequest;
import com.swcms.model.PickupStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PickupRequestRepository extends JpaRepository<PickupRequest, Long> {
    List<PickupRequest> findByCitizenId(Long citizenId);
    List<PickupRequest> findByStatus(PickupStatus status);
}
