package EmployeeApplicationMandiri.AgusSupriyanto_Programmer.service;

import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.dto.LoginRequest;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.dto.LoginResponse;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.dto.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    LoginResponse refreshToken(String refreshToken);
}
