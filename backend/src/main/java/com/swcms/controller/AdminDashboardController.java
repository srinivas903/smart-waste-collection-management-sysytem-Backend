package com.swcms.controller;

import com.swcms.model.Role;
import com.swcms.model.User;
import com.swcms.repository.UserRepository;
import com.swcms.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    private final DashboardService dashboardService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> stats() {
        return ResponseEntity.ok(dashboardService.getStats());
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    // Admin creates a STAFF or ADMIN account directly
    @PostMapping("/staff")
    public ResponseEntity<User> createStaff(@RequestBody Map<String, String> body) {
        User staff = User.builder()
                .name(body.get("name"))
                .email(body.get("email"))
                .password(passwordEncoder.encode(body.get("password")))
                .phone(body.get("phone"))
                .role(Role.valueOf(body.getOrDefault("role", "STAFF")))
                .build();
        return ResponseEntity.ok(userRepository.save(staff));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
