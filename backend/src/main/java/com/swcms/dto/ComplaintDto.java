package com.swcms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ComplaintDto {
    @NotBlank
    private String subject;

    @NotBlank
    private String description;

    private Long relatedPickupRequestId;
}
