package com.exp.cl.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.exp.cl.user.business.UserService;
import com.exp.cl.user.models.dto.GetUserResponseDTO;
import com.exp.cl.user.models.dto.UserDTO;
import com.exp.cl.user.models.dto.UserResponseDTO;
import com.exp.cl.user.models.dto.UserStateRequestDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Api(tags = "UserController Api")
public class UserController {

    private final UserService userService;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a new user", notes = "Create a user with provided details")
    @ApiResponses({
        @ApiResponse(code = 201, message = "User created successfully"),
        @ApiResponse(code = 400, message = "Invalid input provided"),
    })
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserDTO userDTOMono) {
        return  ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDTOMono));
    }

    @GetMapping("/{id}")    
    @ApiOperation(value = "Get user", notes = "show a user by Id")
    @ApiResponses({
        @ApiResponse(code = 200, message = "User created successfully"),
        @ApiResponse(code = 404, message = "Not found"),
    })
    public ResponseEntity<GetUserResponseDTO> getUser(@PathVariable UUID id) {
        return  ResponseEntity.ok(userService.getUser(id));
    }

    @DeleteMapping("/{id}")    
    @ApiOperation(value = "Delete by  user", notes = "delete a user by Id")
    @ApiResponses({
        @ApiResponse(code = 200, message = "User deleted successfully"),
        @ApiResponse(code = 404, message = "Not found"),
    })
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
            userService.deleteUser(id);
        return  ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")    
    @ApiOperation(value = "update state  user")
    @ApiResponses({
        @ApiResponse(code = 200, message = "User modify successfully"),
        @ApiResponse(code = 404, message = "Not found"),
    })
    public ResponseEntity<GetUserResponseDTO> stateUser(@PathVariable UUID id , @RequestBody UserStateRequestDTO userDTO) {
        return  ResponseEntity.ok(userService.updateStatus(id, userDTO.isActive()));
    }
}