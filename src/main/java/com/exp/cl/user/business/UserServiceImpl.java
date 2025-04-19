package com.exp.cl.user.business;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.exp.cl.config.PasswordValidator;
import com.exp.cl.mapper.UserMapper;
import com.exp.cl.user.error.EmailAlreadyRegisteredException;
import com.exp.cl.user.error.InvalidPasswordException;
import com.exp.cl.user.error.UserNotFoundException;
import com.exp.cl.user.models.dto.GetUserResponseDTO;
import com.exp.cl.user.models.dto.UserDTO;
import com.exp.cl.user.models.dto.UserResponseDTO;
import com.exp.cl.user.models.entity.User;
import com.exp.cl.user.repository.UserRepository;
import com.exp.cl.utils.JWTUtils;
import com.exp.cl.utils.PBKDF2Encoder;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PBKDF2Encoder PBKDF2Encoder;
    private final JWTUtils jwtUtils;
    private final PasswordValidator  passwordValidator;

    public UserResponseDTO createUser(UserDTO userDTO){
        validatePassowrd(userDTO);
        validatUserOrGetUser(userDTO.getEmail());        
        User saved = userRepository.save(mapUser(userDTO));
            return userMapper.toUserResponseDTO(saved);
    }

    public GetUserResponseDTO getUser(UUID id){
        return userRepository.findById(id)
                 .map(userMapper::toUserGetResponseDTO)
                 .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public void deleteUser(UUID id){
        userRepository.deleteById(id);
    }

    public GetUserResponseDTO updateStatus(UUID id, boolean isActive) {
                 User user = userRepository.findById(id)
                         .orElseThrow(() -> new UserNotFoundException("User not found"));
                 user.setActive(isActive);
                 user.setModifyAt(LocalDateTime.now());
                 return userMapper.toUserGetResponseDTO(userRepository.save(user));
    }

    private User mapUser(UserDTO userDTO){
        User user = userMapper.toEntity(userDTO);
        user.setPassword(PBKDF2Encoder.encode(userDTO.getPassword()));
        String token = jwtUtils.generateToken(userDTO.getName(), userDTO.getEmail()); 
        user.setToken(token);
            if (user.getPhones() != null) {
                user.getPhones().forEach(phone -> phone.setUser(user));
            }
        return user;
    }
    private void validatePassowrd(UserDTO userDTO){
        if (!passwordValidator.isValid(userDTO.getPassword())) {
            throw new InvalidPasswordException("Password not cumple con los requistos basicos");
        }
    }

    private void validatUserOrGetUser(String email ){
        userRepository.findByEmail(email).
        ifPresent( user -> { 
            throw new EmailAlreadyRegisteredException("El correo ya se encuentra registrado : " + email);
        });
    }
} 
