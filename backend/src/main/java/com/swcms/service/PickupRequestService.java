package com.swcms.service;

import com.swcms.dto.PickupRequestDto;
import com.swcms.exception.ResourceNotFoundException;
import com.swcms.model.PickupRequest;
import com.swcms.model.PickupStatus;
import com.swcms.model.User;
import com.swcms.repository.PickupRequestRepository;
import com.swcms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PickupRequestService {

    private final PickupRequestRepository pickupRequestRepository;
    private final UserRepository userRepository;

    public PickupRequest create(Long citizenId, PickupRequestDto dto) {
        User citizen = userRepository.findById(citizenId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        PickupRequest request = PickupRequest.builder()
                .citizen(citizen)
                .pickupAddress(dto.getPickupAddress())
                .wasteType(dto.getWasteType())
                .preferredDate(dto.getPreferredDate())
                .notes(dto.getNotes())
                .status(PickupStatus.PENDING)
                .build();

        return pickupRequestRepository.save(request);
    }

    public List<PickupRequest> getAll() {
        return pickupRequestRepository.findAll();
    }

    public List<PickupRequest> getByCitizen(Long citizenId) {
        return pickupRequestRepository.findByCitizenId(citizenId);
    }

    public List<PickupRequest> getByStatus(PickupStatus status) {
        return pickupRequestRepository.findByStatus(status);
    }

    public PickupRequest getById(Long id) {
        return pickupRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pickup request not found"));
    }

    public PickupRequest updateStatus(Long id, PickupStatus status) {
        PickupRequest request = getById(id);
        request.setStatus(status);
        return pickupRequestRepository.save(request);
    }

    public PickupRequest cancel(Long id, Long citizenId) {
        PickupRequest request = getById(id);
        if (!request.getCitizen().getId().equals(citizenId)) {
            throw new ResourceNotFoundException("Pickup request not found for this user");
        }
        request.setStatus(PickupStatus.CANCELLED);
        return pickupRequestRepository.save(request);
    }
}
