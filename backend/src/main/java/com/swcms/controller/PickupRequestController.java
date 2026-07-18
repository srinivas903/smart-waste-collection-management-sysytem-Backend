package com.swcms.controller;

import com.swcms.dto.PickupRequestDto;
import com.swcms.dto.StatusUpdateDto;
import com.swcms.model.PickupRequest;
import com.swcms.model.PickupStatus;
import com.swcms.security.UserPrincipal;
import com.swcms.service.PickupRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pickup-requests")
@RequiredArgsConstructor
public class PickupRequestController {

    private final PickupRequestService pickupRequestService;

    // Citizen creates a new pickup request
    @PostMapping
    @PreAuthorize("hasRole('CITIZEN')")
    public ResponseEntity<PickupRequest> create(@AuthenticationPrincipal UserPrincipal principal,
                                                 @Valid @RequestBody PickupRequestDto dto) {
        return ResponseEntity.ok(pickupRequestService.create(principal.getId(), dto));
    }

    // Citizen: view own requests. Staff/Admin: view all (via query params)
    @GetMapping
    public ResponseEntity<List<PickupRequest>> getAll(@AuthenticationPrincipal UserPrincipal principal,
                                                        @RequestParam(required = false) PickupStatus status) {
        if (principal.getRole().equals("CITIZEN")) {
            return ResponseEntity.ok(pickupRequestService.getByCitizen(principal.getId()));
        }
        if (status != null) {
            return ResponseEntity.ok(pickupRequestService.getByStatus(status));
        }
        return ResponseEntity.ok(pickupRequestService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PickupRequest> getById(@PathVariable Long id) {
        return ResponseEntity.ok(pickupRequestService.getById(id));
    }

    // Staff/Admin update status manually (e.g., mark collected)
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<PickupRequest> updateStatus(@PathVariable Long id, @RequestBody StatusUpdateDto dto) {
        return ResponseEntity.ok(pickupRequestService.updateStatus(id, PickupStatus.valueOf(dto.getStatus())));
    }

    // Citizen cancels their own request
    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('CITIZEN')")
    public ResponseEntity<PickupRequest> cancel(@AuthenticationPrincipal UserPrincipal principal,
                                                 @PathVariable Long id) {
        return ResponseEntity.ok(pickupRequestService.cancel(id, principal.getId()));
    }
}
