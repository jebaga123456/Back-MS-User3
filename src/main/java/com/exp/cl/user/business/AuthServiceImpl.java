package com.exp.cl.user.business;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

import com.exp.cl.user.error.InvalidPasswordException;
import com.exp.cl.user.error.UserNotFoundException;
import com.exp.cl.user.models.dto.LoginResponseDTO;
import com.exp.cl.user.models.entity.User;
import com.exp.cl.user.repository.UserRepository;
import com.exp.cl.utils.Constants;
import com.exp.cl.utils.JWTUtils;
import com.exp.cl.utils.PBKDF2Encoder;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PBKDF2Encoder PBKDF2Encoder;
    private final JWTUtils jwtUtils;

   public LoginResponseDTO login(String email, String password) {
        User userFind = this.validatUserOrGetUser(email);
        validatePassword(password, userFind);
         String token = jwtUtils.generateToken(userFind.getName(),userFind.getEmail());
         this.saveUser(userFind, token);
            return LoginResponseDTO.builder().token(token).build();            
    }

   private void saveUser(User userFind, String token){
      userFind.setToken(token);
      userFind.setLastLogin(LocalDateTime.now());
       userRepository.save(userFind);            
   }

   private User validatUserOrGetUser(String email){
         return userRepository.findByEmail(email).
                  orElseThrow(() ->new UserNotFoundException("Usuario no registrado: " + email));
    }

   private void validatePassword(String password, User userFind ){
      if (!PBKDF2Encoder.matches(password, userFind.getPassword())) {
         throw new InvalidPasswordException("Contraseña invalida");
     }
   }

   @Override
   public void logout(String token) {        
       String email = jwtUtils.getUsernameFromToken(obtainToken(token));
            
       User userFind = this.validatUserOrGetUser(email);
           userFind.setToken(null);
         userRepository.save(userFind);        
     }

    private String obtainToken(String token){
        String tokenReplace = token ;
        if(token.startsWith(Constants.PREFIX_TOKEN))
        tokenReplace = token.substring(7);

    return tokenReplace;
    }
} 
