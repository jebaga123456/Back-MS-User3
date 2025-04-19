package com.exp.cl.user.business;

import java.util.UUID;
import com.exp.cl.user.models.dto.GetUserResponseDTO;
import com.exp.cl.user.models.dto.UserDTO;
import com.exp.cl.user.models.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO createUser(UserDTO userDTO);
    GetUserResponseDTO getUser(UUID id);
    void deleteUser(UUID id);
    GetUserResponseDTO updateStatus(UUID id, boolean isActive);
} 
