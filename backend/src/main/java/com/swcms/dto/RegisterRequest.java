package com.swcms.dto;

import com.swcms.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String phone;
    private String address;

    // CITIZEN by default; ADMIN can create STAFF/ADMIN accounts via admin endpoints
    private Role role;
}
