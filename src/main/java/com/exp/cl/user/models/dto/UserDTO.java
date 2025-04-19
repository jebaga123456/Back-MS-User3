package com.exp.cl.user.models.dto;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class UserDTO {
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Pattern(
      regexp = "^[\\w\\.-]+@[\\w\\.-]+\\.cl$",
      message = "Email must be valid and end with .cl"
    )
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    private List<PhoneDTO> phones;
}