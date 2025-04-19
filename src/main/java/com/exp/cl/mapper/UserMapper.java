package com.exp.cl.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.exp.cl.user.models.dto.GetUserResponseDTO;
import com.exp.cl.user.models.dto.PhoneDTO;
import com.exp.cl.user.models.dto.UserDTO;
import com.exp.cl.user.models.dto.UserResponseDTO;
import com.exp.cl.user.models.entity.Phone;
import com.exp.cl.user.models.entity.User;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "modifyAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "lastLogin", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "phones", source = "phones")
    User toEntity(UserDTO userDTO);

    List<Phone> phoneDtoListToEntity(List<PhoneDTO> phoneDTOs);
    
    Phone phoneDtoToEntity(PhoneDTO phoneDTO);
    
    @Mapping(source = "createAt", target = "created")
    @Mapping(source = "modifyAt", target = "modified")
    @Mapping(source = "lastLogin", target = "lastLogin")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "active", target = "active")
    UserResponseDTO toUserResponseDTO(User user);

    GetUserResponseDTO toUserGetResponseDTO(User user);
}