package com.duchung.vn.controller;

import com.duchung.vn.dto.LoginRequest;
import com.duchung.vn.dto.LoginResponse;
import com.duchung.vn.dto.UserCreateRequest;
import com.duchung.vn.dto.UserDTO;
import com.duchung.vn.exception.BadRequestException;
import com.duchung.vn.security.JwtTokenProvider;
import com.duchung.vn.service.UserService;
import com.duchung.vn.utils.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "Login with username and password")
    public ResponseEntity<ResponseUtils.ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication);

        UserDTO user = userService.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new BadRequestException("User not found"));

        LoginResponse response = new LoginResponse(jwt, user);

        return ResponseUtils.success(response, "Login successful");
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Username will be automatically generated from full name if not provided")
    public ResponseEntity<ResponseUtils.ApiResponse<UserDTO>> register(@Valid @RequestBody UserCreateRequest request) {
        UserDTO createdUser = userService.createUser(request);
        return ResponseUtils.created(createdUser, "User registered successfully");
    }
}