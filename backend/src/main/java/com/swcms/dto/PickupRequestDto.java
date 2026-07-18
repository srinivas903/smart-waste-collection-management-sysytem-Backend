package com.swcms.dto;

import com.swcms.model.WasteType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PickupRequestDto {
    @NotBlank
    private String pickupAddress;

    @NotNull
    private WasteType wasteType;

    @NotNull
    private LocalDate preferredDate;

    private String notes;
}
