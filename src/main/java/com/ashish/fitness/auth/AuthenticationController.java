package com.ashish.fitness.auth;

import com.ashish.fitness.user.User;
import com.ashish.fitness.user.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("auth")
@Tag(name = "Authentication")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest request
    ) throws MessagingException {
        authenticationService.register(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/verify")
    public ResponseEntity<Boolean> verifyUser(@RequestParam("email") String email) {
        boolean isVerified = authenticationService.verifyUser(email);
        return ResponseEntity.ok(isVerified); // Return boolean response
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<Object> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(user);
            return ResponseEntity.ok().body("{\"message\": \"Password reset successfully\"}");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Failed to update password\"}");
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping("/activate-account")
    public void confirm(
        @RequestParam String token
    ) throws MessagingException {
        authenticationService.activateAccount(token);
    }

}
