package com.ltc.patientservice.controller;

import com.ltc.patientservice.dto.auth.*;
import com.ltc.patientservice.entity.RefreshToken;
import com.ltc.patientservice.entity.Role;
import com.ltc.patientservice.entity.User;
import com.ltc.patientservice.exception.TokenRefreshException;
import com.ltc.patientservice.repository.UserRepository;
import com.ltc.patientservice.service.auth.JwtService;
import com.ltc.patientservice.service.auth.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: Email is already in use!");
        }

        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        log.info("Sending user registration event for {}", user.getEmail());

        var accessToken = jwtService.generateToken(user);
        var refreshToken =
                refreshTokenService.createRefreshToken(user.getEmail());

        return ResponseEntity.ok(
                new AuthResponse(
                        accessToken,
                        refreshToken.getToken()
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        var accessToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken.getToken()));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        return refreshTokenService.findByToken(request.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateToken(user);
                    return ResponseEntity.ok(new AuthResponse(accessToken, request.getToken()));
                })
                .orElseThrow(() -> new TokenRefreshException(request.getToken(), "Refresh token is not in database!"));
    }
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest()
                    .body(new LogoutResponse("Token not found"));}
        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);
        refreshTokenService.deleteByUserEmail(email);
        return ResponseEntity.ok(new LogoutResponse("Logout successful")
        );
    }
}
