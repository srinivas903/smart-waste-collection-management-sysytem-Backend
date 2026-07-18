package com.swcms.service;

import com.swcms.dto.VehicleDto;
import com.swcms.exception.ResourceNotFoundException;
import com.swcms.model.Vehicle;
import com.swcms.model.VehicleStatus;
import com.swcms.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public Vehicle create(VehicleDto dto) {
        Vehicle vehicle = Vehicle.builder()
                .vehicleNumber(dto.getVehicleNumber())
                .driverName(dto.getDriverName())
                .driverPhone(dto.getDriverPhone())
                .capacityInTons(dto.getCapacityInTons())
                .status(VehicleStatus.AVAILABLE)
                .build();
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getAll() {
        return vehicleRepository.findAll();
    }

    public List<Vehicle> getAvailable() {
        return vehicleRepository.findByStatus(VehicleStatus.AVAILABLE);
    }

    public Vehicle getById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
    }

    public Vehicle updateStatus(Long id, VehicleStatus status) {
        Vehicle vehicle = getById(id);
        vehicle.setStatus(status);
        return vehicleRepository.save(vehicle);
    }

    public void delete(Long id) {
        vehicleRepository.deleteById(id);
    }
}
