package com.swcms.controller;

import com.swcms.dto.ComplaintDto;
import com.swcms.dto.StatusUpdateDto;
import com.swcms.model.Complaint;
import com.swcms.model.ComplaintStatus;
import com.swcms.security.UserPrincipal;
import com.swcms.service.ComplaintService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;

    @PostMapping
    @PreAuthorize("hasRole('CITIZEN')")
    public ResponseEntity<Complaint> create(@AuthenticationPrincipal UserPrincipal principal,
                                             @Valid @RequestBody ComplaintDto dto) {
        return ResponseEntity.ok(complaintService.create(principal.getId(), dto));
    }

    @GetMapping
    public ResponseEntity<List<Complaint>> getAll(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal.getRole().equals("CITIZEN")) {
            return ResponseEntity.ok(complaintService.getByCitizen(principal.getId()));
        }
        return ResponseEntity.ok(complaintService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Complaint> getById(@PathVariable Long id) {
        return ResponseEntity.ok(complaintService.getById(id));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<Complaint> updateStatus(@PathVariable Long id, @RequestBody StatusUpdateDto dto) {
        return ResponseEntity.ok(complaintService.updateStatus(id, ComplaintStatus.valueOf(dto.getStatus()), dto.getNote()));
    }
}
