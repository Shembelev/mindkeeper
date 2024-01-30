package com.mshembelev.mindskeeper.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuccessResponse {
    private String message;
    private Integer status;
}
