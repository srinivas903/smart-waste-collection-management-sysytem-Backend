package com.swcms.controller;

import com.swcms.dto.VehicleDto;
import com.swcms.dto.StatusUpdateDto;
import com.swcms.model.Vehicle;
import com.swcms.model.VehicleStatus;
import com.swcms.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Vehicle> create(@Valid @RequestBody VehicleDto dto) {
        return ResponseEntity.ok(vehicleService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAll(@RequestParam(required = false) Boolean availableOnly) {
        if (Boolean.TRUE.equals(availableOnly)) {
            return ResponseEntity.ok(vehicleService.getAvailable());
        }
        return ResponseEntity.ok(vehicleService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getById(id));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<Vehicle> updateStatus(@PathVariable Long id, @RequestBody StatusUpdateDto dto) {
        return ResponseEntity.ok(vehicleService.updateStatus(id, VehicleStatus.valueOf(dto.getStatus())));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
