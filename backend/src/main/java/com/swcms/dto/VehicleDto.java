package com.swcms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VehicleDto {
    @NotBlank
    private String vehicleNumber;

    @NotBlank
    private String driverName;

    private String driverPhone;
    private Double capacityInTons;
}
