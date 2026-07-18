package com.swcms.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AssignmentDto {
    @NotNull
    private Long pickupRequestId;

    @NotNull
    private Long vehicleId;

    @NotNull
    private LocalDate scheduledDate;
}
