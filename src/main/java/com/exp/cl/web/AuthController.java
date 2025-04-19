package com.exp.cl.web;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.exp.cl.user.business.AuthService;
import com.exp.cl.user.models.dto.LoginRequestDTO;
import com.exp.cl.user.models.dto.LoginResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Api(value = "AuthController API", description = "Auth methods")
public class AuthController {
    
    private final AuthService authService;

    
    @PostMapping( value = "/login", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(value = "Login a user", notes = "Login using email and password")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Login successful"),
        @ApiResponse(code = 401, message = "Invalid credentials"),
    })
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(response); // HTTP 200 OK
    }
    @PostMapping( value = "/logout" , produces = { MediaType.APPLICATION_STREAM_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(value = "Logout user",
			consumes = MediaType.APPLICATION_JSON_VALUE,			
            httpMethod = "POST")	
	 @ApiResponses(value = {    		
     		 @ApiResponse(code = 200, message = "Éxito"),
             @ApiResponse(code = 401, message = "No autorizado"),
             @ApiResponse(code = 403, message = "El acceso esta prohibido"),             
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
    }
}
