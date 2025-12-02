package com.takehome.stayease.Service;

import com.takehome.stayease.dto.*;

public interface UserService {
    LoginResponse registerUser(UserRegistrationRequest request);
    LoginResponse loginUser(LoginRequest request);
}
