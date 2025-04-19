package com.exp.cl.user.models.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginResponseDTO {
    private String token;
}
