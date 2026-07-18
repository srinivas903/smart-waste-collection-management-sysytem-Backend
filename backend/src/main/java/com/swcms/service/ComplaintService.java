package com.swcms.service;

import com.swcms.dto.ComplaintDto;
import com.swcms.exception.ResourceNotFoundException;
import com.swcms.model.Complaint;
import com.swcms.model.ComplaintStatus;
import com.swcms.model.PickupRequest;
import com.swcms.model.User;
import com.swcms.repository.ComplaintRepository;
import com.swcms.repository.PickupRequestRepository;
import com.swcms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final PickupRequestRepository pickupRequestRepository;

    public Complaint create(Long citizenId, ComplaintDto dto) {
        User citizen = userRepository.findById(citizenId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        PickupRequest related = null;
        if (dto.getRelatedPickupRequestId() != null) {
            related = pickupRequestRepository.findById(dto.getRelatedPickupRequestId())
                    .orElseThrow(() -> new ResourceNotFoundException("Related pickup request not found"));
        }

        Complaint complaint = Complaint.builder()
                .citizen(citizen)
                .relatedPickupRequest(related)
                .subject(dto.getSubject())
                .description(dto.getDescription())
                .status(ComplaintStatus.OPEN)
                .build();

        return complaintRepository.save(complaint);
    }

    public List<Complaint> getAll() {
        return complaintRepository.findAll();
    }

    public List<Complaint> getByCitizen(Long citizenId) {
        return complaintRepository.findByCitizenId(citizenId);
    }

    public Complaint getById(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found"));
    }

    public Complaint updateStatus(Long id, ComplaintStatus status, String note) {
        Complaint complaint = getById(id);
        complaint.setStatus(status);
        if (note != null) {
            complaint.setResolutionNotes(note);
        }
        if (status == ComplaintStatus.RESOLVED) {
            complaint.setResolvedAt(LocalDateTime.now());
        }
        return complaintRepository.save(complaint);
    }
}
