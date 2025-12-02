package com.takehome.stayease.Service;





import com.takehome.stayease.dto.*;
import com.takehome.stayease.entity.User;
import com.takehome.stayease.enums.Role;
import com.takehome.stayease.exceptions.BadRequestException;
import com.takehome.stayease.exceptions.ResourceNotFoundException;
import com.takehome.stayease.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectProvider<ModelMapper> modelMapperProvider;

    @Override
    public LoginResponse registerUser(UserRegistrationRequest request) {

        if (!isValidEmail(request.getEmail())) {
            throw new BadRequestException("Invalid email format");
        }

        // Check for duplicate email
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("User already exists");
        }

        // Validate password
        String password = request.getPassword();
        if (!isValidPassword(password)) {
            throw new BadRequestException("Password must be at least 8 characters long and contain at least one uppercase letter, one number, and one special character.");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() == null ? Role.CUSTOMER : Role.valueOf(request.getRole()));

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());
        return new LoginResponse(token);
    }

    @Override
    public LoginResponse loginUser(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("Invalid credentials");
        }

        User user = userOptional.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getEmail());
        return new LoginResponse(token);

    }

    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(regex);
    }

    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && email.matches(regex);
    }

}