package com.swcms.controller;

import com.swcms.dto.AssignmentDto;
import com.swcms.dto.StatusUpdateDto;
import com.swcms.model.AssignmentStatus;
import com.swcms.model.VehicleAssignment;
import com.swcms.security.UserPrincipal;
import com.swcms.service.VehicleAssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
public class VehicleAssignmentController {

    private final VehicleAssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<VehicleAssignment> assign(@AuthenticationPrincipal UserPrincipal principal,
                                                      @Valid @RequestBody AssignmentDto dto) {
        return ResponseEntity.ok(assignmentService.assign(principal.getId(), dto));
    }

    @GetMapping
    public ResponseEntity<List<VehicleAssignment>> getAll() {
        return ResponseEntity.ok(assignmentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleAssignment> getById(@PathVariable Long id) {
        return ResponseEntity.ok(assignmentService.getById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<VehicleAssignment> updateStatus(@PathVariable Long id, @RequestBody StatusUpdateDto dto) {
        return ResponseEntity.ok(assignmentService.updateStatus(id, AssignmentStatus.valueOf(dto.getStatus())));
    }
}
