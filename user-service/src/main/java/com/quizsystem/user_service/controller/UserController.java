package com.quizsystem.user_service.controller;

import com.quizsystem.user_service.model.User;
import com.quizsystem.user_service.service.CustomUserDetailsService;
import com.quizsystem.user_service.service.UserService;
import com.quizsystem.user_service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @GetMapping("/test")
    public String test() {
        return "User Service is working";
    }

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    //
    // --- THIS IS THE NEW METHOD TO ADD ---
    //
    /**
     * This endpoint retrieves the full User object for the currently authenticated user.
     * It uses the Principal object, which Spring Security populates from the JWT,
     * to find the user by their username.
     */
    @GetMapping("/me")
    public ResponseEntity<User> getMyDetails(Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        return ResponseEntity.ok(user);
    }
}