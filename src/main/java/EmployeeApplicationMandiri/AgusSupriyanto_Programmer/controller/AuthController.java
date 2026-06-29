package EmployeeApplicationMandiri.AgusSupriyanto_Programmer.controller;

import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.dto.*;
import EmployeeApplicationMandiri.AgusSupriyanto_Programmer.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register
            (@Valid @RequestBody RegisterRequest request){

        service.register(request);

        return ResponseEntity.ok(ApiResponse.success("Register success", null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request){

        LoginResponse loginResponse = service.login(request);

        return ResponseEntity.ok(
                ApiResponse.success("Login success",loginResponse)
        );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(
            @RequestBody RefreshTokenRequest request
            ){

        LoginResponse response =
                service.refreshToken(request.getRefreshToken());

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Token refresh succesfully",
                        response
                )
        );
    }


}
