package com.furniflow.service;

import com.furniflow.dto.RegisterRequest;
import com.furniflow.entity.User;

public interface UserService {

    User registerUser(RegisterRequest request);
}