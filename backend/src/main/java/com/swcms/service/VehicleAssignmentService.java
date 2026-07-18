package com.swcms.service;

import com.swcms.dto.AssignmentDto;
import com.swcms.exception.BadRequestException;
import com.swcms.exception.ResourceNotFoundException;
import com.swcms.model.*;
import com.swcms.repository.PickupRequestRepository;
import com.swcms.repository.UserRepository;
import com.swcms.repository.VehicleAssignmentRepository;
import com.swcms.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleAssignmentService {

    private final VehicleAssignmentRepository assignmentRepository;
    private final PickupRequestRepository pickupRequestRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    public VehicleAssignment assign(Long staffId, AssignmentDto dto) {
        PickupRequest pickupRequest = pickupRequestRepository.findById(dto.getPickupRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Pickup request not found"));

        if (pickupRequest.getStatus() == PickupStatus.COLLECTED
                || pickupRequest.getStatus() == PickupStatus.CANCELLED) {
            throw new BadRequestException("Cannot assign a vehicle to a request that is " + pickupRequest.getStatus());
        }

        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        if (vehicle.getStatus() == VehicleStatus.MAINTENANCE) {
            throw new BadRequestException("Vehicle is under maintenance and cannot be assigned");
        }

        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff user not found"));

        VehicleAssignment assignment = VehicleAssignment.builder()
                .pickupRequest(pickupRequest)
                .vehicle(vehicle)
                .assignedByStaff(staff)
                .scheduledDate(dto.getScheduledDate())
                .status(AssignmentStatus.SCHEDULED)
                .build();

        assignment = assignmentRepository.save(assignment);

        pickupRequest.setStatus(PickupStatus.ASSIGNED);
        pickupRequestRepository.save(pickupRequest);

        vehicle.setStatus(VehicleStatus.ON_DUTY);
        vehicleRepository.save(vehicle);

        return assignment;
    }

    public List<VehicleAssignment> getAll() {
        return assignmentRepository.findAll();
    }

    public VehicleAssignment getById(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));
    }

    public VehicleAssignment updateStatus(Long id, AssignmentStatus status) {
        VehicleAssignment assignment = getById(id);
        assignment.setStatus(status);

        if (status == AssignmentStatus.COMPLETED) {
            assignment.getPickupRequest().setStatus(PickupStatus.COLLECTED);
            pickupRequestRepository.save(assignment.getPickupRequest());

            Vehicle vehicle = assignment.getVehicle();
            vehicle.setStatus(VehicleStatus.AVAILABLE);
            vehicleRepository.save(vehicle);
        } else if (status == AssignmentStatus.CANCELLED) {
            Vehicle vehicle = assignment.getVehicle();
            vehicle.setStatus(VehicleStatus.AVAILABLE);
            vehicleRepository.save(vehicle);
        }

        return assignmentRepository.save(assignment);
    }
}
