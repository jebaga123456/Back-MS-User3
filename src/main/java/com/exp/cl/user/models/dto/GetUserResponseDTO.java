package com.exp.cl.user.models.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserResponseDTO {
    
    private String name;    
    private String email;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;
    private LocalDateTime lastLogin;
    private boolean active;
    private List<PhoneDTO> phones;
}