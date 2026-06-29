package EmployeeApplicationMandiri.AgusSupriyanto_Programmer;

import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.dto.LoginRequest;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.dto.LoginResponse;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.dto.RegisterRequest;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.entity.User;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.exception.InvalidCrendentialException;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.exception.UsernameAlreadyExistsException;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.repository.UserRepository;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.service.AuthServiceImpl;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.service.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void registerSuccess(){

        RegisterRequest request = new RegisterRequest();

        request.setUsername("Agus");
        request.setPassword("123456");

        when(userRepository.existsByUsername("Agus"))
                .thenReturn(false);

        when(passwordEncoder.encode("123456"))
                .thenReturn("encodedPassword");

        authService.register(request);

        verify(userRepository, times(1)).save(org.mockito.ArgumentMatchers.any());

    }

    @Test
    void registerFailedUsernameAlreadyExists(){

        RegisterRequest request = new RegisterRequest();

        request.setUsername("Agus");
        request.setPassword("123456");

        when(userRepository.existsByUsername("Agus"))
                .thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class,
                ()-> authService.register(request)
                );

        verify(userRepository, never()).save(any());
    }

    @Test
    void loginSuccess(){

        LoginRequest request = new LoginRequest();

        request.setUsername("Agus");
        request.setPassword("123456");

        User user = User.builder()
                        .username("Agus")
                                .password("encodedPassword")
                                        .build();

        when(userRepository.findByUsername("Agus"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "123456",
                "encodedPassword"
        ))
                .thenReturn(true);

        when(jwtService.generateAccessToken("Agus"))
                .thenReturn("access-token");
        when(jwtService.generateRefreshToken("Agus"))
                .thenReturn("refresh-token");

        //Act
        LoginResponse response = authService.login(request);

        //Assert
        assertEquals(
                "access-token",
                response.getAccessToken()
        );

        assertEquals(
                "refresh-token",
                response.getRefreshToken()
        );

        verify(userRepository,times(1))
                .findByUsername("Agus");
    }


    @Test
    void loginFailedUserNotFound(){

        LoginRequest request = new LoginRequest();

        request.setUsername("Agus");
        request.setPassword("123456");

        when(userRepository.findByUsername("Agus"))
                .thenReturn(Optional.empty());

        InvalidCrendentialException exception =
                assertThrows(InvalidCrendentialException.class,
                () -> authService.login(request));

        assertEquals(
                "Username or Password Wrong",
                exception.getMessage()
        );
    }

    @Test
    void loginFailedWrongPassword(){

        LoginRequest request = new LoginRequest();

        request.setUsername("Agus");
        request.setPassword("123456");

        User user = User.builder()
                .username("Agus")
                .password("encodedPassword")
                .build();

        when(userRepository.findByUsername("Agus"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "123456",
                "encodedPassword"
        )).thenReturn(false);

        InvalidCrendentialException exception =
                assertThrows(
                        InvalidCrendentialException.class,
                        () -> authService.login(request)
                );

        assertEquals("Username or Password Wrong",
                exception.getMessage());

    }

    @Test
    void refreshTokenSuccess(){


        String oldRefreshToken = "old-refresh-token";

        when(jwtService.isTokenValid(oldRefreshToken))
                .thenReturn(true);

        when(jwtService.extraUsername(oldRefreshToken))
                .thenReturn("Agus");

        when(jwtService.generateAccessToken("Agus"))
                .thenReturn("new-access-token");

        when(jwtService.generateRefreshToken("Agus"))
                .thenReturn("new-refresh-token");

        LoginResponse response =
                authService.refreshToken(oldRefreshToken);

        assertEquals(
                "new-access-token",
                response.getAccessToken()
        );

        assertEquals(
                "new-refresh-token",
                response.getRefreshToken()
        );

    }

    @Test
    void refreshTokenFailedInvalidToken(){


        String refreshToken = "invalid-token";

        when(jwtService.isTokenValid(refreshToken))
                .thenReturn(false);

        InvalidCrendentialException exception =
                assertThrows(
                        InvalidCrendentialException.class,
                        () -> authService.refreshToken(refreshToken)
                );

        assertEquals(
                "Invalid Refresh Token",
                exception.getMessage()
        );


    }

}
