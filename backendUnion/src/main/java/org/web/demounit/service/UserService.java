package org.web.demounit.service;

import org.web.demounit.persists.entities.Role;
import org.web.demounit.persists.entities.Status;
import org.web.demounit.persists.entities.User;
import org.web.demounit.persists.repositories.UserRepository;
import org.web.demounit.controllers.request.AuthenticationRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User register(AuthenticationRequestDTO request) {
        User user = findByEmail(request.getEmail());
            if (user != null) {
                throw new IllegalStateException("This email already engaged");
            }

        User item = new User();
            item.setEmail(request.getEmail());
            item.setFirstname(request.getUsername());
            String password = passwordEncoder.encode(request.getPassword());
            item.setPassword(password);
            //default role USER
            item.setRole(Role.USER);
            //default status ACTIVE
            item.setStatus(Status.ACTIVE);

        return userRepository.save(item);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


}
