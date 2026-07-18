package com.swcms.service;

import com.swcms.model.*;
import com.swcms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final PickupRequestRepository pickupRequestRepository;
    private final VehicleRepository vehicleRepository;
    private final ComplaintRepository complaintRepository;

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalCitizens", userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.CITIZEN).count());
        stats.put("totalStaff", userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.STAFF).count());

        stats.put("totalPickupRequests", pickupRequestRepository.count());
        stats.put("pendingPickupRequests", pickupRequestRepository.findByStatus(PickupStatus.PENDING).size());
        stats.put("assignedPickupRequests", pickupRequestRepository.findByStatus(PickupStatus.ASSIGNED).size());
        stats.put("collectedPickupRequests", pickupRequestRepository.findByStatus(PickupStatus.COLLECTED).size());
        stats.put("cancelledPickupRequests", pickupRequestRepository.findByStatus(PickupStatus.CANCELLED).size());

        stats.put("totalVehicles", vehicleRepository.count());
        stats.put("availableVehicles", vehicleRepository.findByStatus(VehicleStatus.AVAILABLE).size());
        stats.put("onDutyVehicles", vehicleRepository.findByStatus(VehicleStatus.ON_DUTY).size());
        stats.put("maintenanceVehicles", vehicleRepository.findByStatus(VehicleStatus.MAINTENANCE).size());

        stats.put("totalComplaints", complaintRepository.count());
        stats.put("openComplaints", complaintRepository.findByStatus(ComplaintStatus.OPEN).size());
        stats.put("inProgressComplaints", complaintRepository.findByStatus(ComplaintStatus.IN_PROGRESS).size());
        stats.put("resolvedComplaints", complaintRepository.findByStatus(ComplaintStatus.RESOLVED).size());

        return stats;
    }
}
