package com.swcms.dto;

import lombok.Data;

@Data
public class StatusUpdateDto {
    private String status;
    private String note; // optional resolution/notes text
}
