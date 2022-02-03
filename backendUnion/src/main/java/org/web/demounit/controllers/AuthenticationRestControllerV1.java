package org.web.demounit.controllers;

import org.web.demounit.controllers.request.AuthenticationRequestDTO;
import org.web.demounit.persists.entities.User;
import org.web.demounit.security.JwtTokenProvider;
import org.web.demounit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationRestControllerV1 {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;

    @PostMapping("/auth/login")
    public ResponseEntity<?> authenticate(@RequestBody @Valid AuthenticationRequestDTO request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<Object, Object> response = new HashMap<>();

            for(int i = 0; i < fieldErrors.size(); i++) {
                response.put(fieldErrors.get(i).getField(), fieldErrors.get(i).getDefaultMessage());
            }

            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        try {
            String email = request.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.getPassword()));
            User user = userService.findByEmail(email);
                if (user == null) {
                    throw new UsernameNotFoundException("User doesn't exists");
                }
            String token = jwtTokenProvider.createToken(email, user.getRole().name());
            Map<Object, Object> response = new HashMap<>();
                response.put("email", email);
                response.put("token", token);

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            Map<Object, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody @Valid AuthenticationRequestDTO request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<Object, Object> response = new HashMap<>();

            for(int i = 0; i < fieldErrors.size(); i++) {
                response.put(fieldErrors.get(i).getField(), fieldErrors.get(i).getDefaultMessage());
            }

            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        try {
            User registerUser = userService.register(request);
            String token = jwtTokenProvider.createToken(request.getEmail(), registerUser.getRole().name());
            Map<Object, Object> response = new HashMap<>();
                response.put("id", registerUser.getId());
                response.put("username", registerUser.getFirstname());
                response.put("email", registerUser.getEmail());
                response.put("token", token);

            return ResponseEntity.ok(response);

        }catch (Exception e) {
            Map<Object, Object> response = new HashMap<>();
                response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/auth/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }
}
