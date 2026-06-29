package EmployeeApplicationMandiri.AgusSupriyanto_Programmer.service;

import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.dto.LoginRequest;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.dto.LoginResponse;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.dto.RegisterRequest;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.entity.User;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.exception.InvalidCrendentialException;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.exception.UsernameAlreadyExistsException;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Override
    public  void register(RegisterRequest request){

        if (repository.existsByUsername((request.getUsername()))){
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.USER)
                .build();

        repository.save(user);
    }

    @Override
    public LoginResponse login(LoginRequest request){

        User user = repository.findByUsername(request.getUsername())
                .orElseThrow(()->
                        new InvalidCrendentialException("Username or Password Wrong"));

        boolean isPasswordValid = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!isPasswordValid){
            throw new InvalidCrendentialException("Username or Password Wrong");
        }

        String accessToken =
                jwtService.generateAccessToken(user.getUsername());

        String refreshToken=
                jwtService.generateRefreshToken(user.getUsername());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public LoginResponse refreshToken(String refreshToken){

        //Cek token valid atau tidak
        if (!jwtService.isTokenValid(refreshToken)) {
            throw new InvalidCrendentialException("Invalid Refresh Token");
        }

        //Ambil Username dari token
        String username =jwtService.extraUsername(refreshToken);

        //Generate token baru
        String newAccessToken=
                jwtService.generateAccessToken(username);

        String newRefreshToken=
                jwtService.generateRefreshToken(username);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();

    }
}
