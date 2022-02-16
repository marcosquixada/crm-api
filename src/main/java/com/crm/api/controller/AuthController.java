package com.crm.api.controller;

import com.crm.api.exception.TokenRefreshException;
import com.crm.api.model.ERole;
import com.crm.api.model.RefreshToken;
import com.crm.api.model.Role;
import com.crm.api.model.User;
import com.crm.api.payload.request.LogOutRequest;
import com.crm.api.payload.request.LoginRequest;
import com.crm.api.payload.request.SignupRequest;
import com.crm.api.payload.request.TokenRefreshRequest;
import com.crm.api.payload.response.JwtResponse;
import com.crm.api.payload.response.MessageResponse;
import com.crm.api.payload.response.TokenRefreshResponse;
import com.crm.api.security.jwt.JwtUtils;
import com.crm.api.security.services.RefreshTokenService;
import com.crm.api.security.services.UserDetailsImpl;
import com.crm.api.service.impl.RoleServiceImpl;
import com.crm.api.service.impl.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    public static final String ERROR_ROLE_IS_NOT_FOUND = "Error: Role is not found.";
    public static final String ERROR_USERNAME_IS_ALREADY_TAKEN = "Error: Username is already taken!";
    public static final String ERROR_EMAIL_IS_ALREADY_IN_USE = "Error: Email is already in use!";

    AuthenticationManager authenticationManager;
    AuthServiceImpl userService;
    PasswordEncoder encoder;
    JwtUtils jwtUtils;
    RoleServiceImpl roleServiceImpl;
    RefreshTokenService refreshTokenService;

    public AuthController(AuthServiceImpl userService,
                          RoleServiceImpl roleServiceImpl,
                          PasswordEncoder encoder,
                          JwtUtils jwtUtils,
                          AuthenticationManager authenticationManager,
                          RefreshTokenService refreshTokenService
                          ){
        this.userService = userService;
        this.roleServiceImpl = roleServiceImpl;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/signup")
    @Operation(summary = "Save User Endpoint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Save a user")
    })
    public ResponseEntity<?> save(
            @Schema(description ="Add user")
            @Parameter(description = "Entry Param", required = true, example = "User")
            @Valid @RequestBody SignupRequest signupRequest){
        if(userService.existsByUsername(signupRequest.getUsername())){
            return ResponseEntity.badRequest().body(new MessageResponse(ERROR_USERNAME_IS_ALREADY_TAKEN));
        }
        if(userService.existsByEmail(signupRequest.getEmail())){
            return ResponseEntity.badRequest().body(new MessageResponse(ERROR_EMAIL_IS_ALREADY_IN_USE));
        }

        User user = new User(signupRequest.getUsername(), signupRequest.getEmail(), encoder.encode(signupRequest.getPassword()));
        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        verifyRole(strRoles, roles);

        user.setRoles(roles);
        userService.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/signin")
    @Operation(summary = "User Authentication")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
        refreshTokenService.deleteByUserId(logOutRequest.getUserId());
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }

    private void verifyRole(Set<String> strRoles, Set<Role> roles) {
        if (strRoles == null) {
            Role userRole = roleServiceImpl.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleServiceImpl.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleServiceImpl.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException(ERROR_ROLE_IS_NOT_FOUND));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleServiceImpl.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
    }
}
