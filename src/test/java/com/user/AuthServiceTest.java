package com.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.exp.cl.user.business.AuthServiceImpl;
import com.exp.cl.user.error.InvalidPasswordException;
import com.exp.cl.user.error.UserNotFoundException;
import com.exp.cl.user.models.dto.LoginResponseDTO;
import com.exp.cl.user.models.entity.User;
import com.exp.cl.user.repository.UserRepository;
import com.exp.cl.utils.JWTUtils;
import com.exp.cl.utils.PBKDF2Encoder;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PBKDF2Encoder encoder;

    @Mock
    private JWTUtils jwtUtils;

    @InjectMocks
    private AuthServiceImpl authService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User();
        mockUser.setId(UUID.randomUUID());
        mockUser.setEmail("test@example.cl");
        mockUser.setPassword("encodedPassword");
        mockUser.setName("Test User");
    }

    @Test
    void testLogin_Successful() {
        when(userRepository.findByEmail("test@example.cl")).thenReturn(Optional.of(mockUser));
        when(encoder.matches("plainPassword", "encodedPassword")).thenReturn(true);
        when(jwtUtils.generateToken("Test User", "test@example.cl")).thenReturn("fake.jwt.token");

        LoginResponseDTO response = authService.login("test@example.cl", "plainPassword");

        assertNotNull(response);
        assertEquals("fake.jwt.token", response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testLogin_InvalidPassword() {
        when(userRepository.findByEmail("test@example.cl")).thenReturn(Optional.of(mockUser));
        when(encoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class, () -> {
            authService.login("test@example.cl", "wrongPassword");
        });

        assertEquals("Contraseña invalida", exception.getMessage());
    }

    @Test
    void testLogin_UserNotFound() {
        when(userRepository.findByEmail("noemail@example.cl")).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            authService.login("noemail@example.cl", "anyPassword");
        });

        assertEquals("Usuario no registrado: noemail@example.cl", exception.getMessage());
    }
}