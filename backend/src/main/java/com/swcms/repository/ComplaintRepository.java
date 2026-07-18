package com.swcms.repository;

import com.swcms.model.Complaint;
import com.swcms.model.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByCitizenId(Long citizenId);
    List<Complaint> findByStatus(ComplaintStatus status);
}
