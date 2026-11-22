package com.furniflow.dto;

import com.furniflow.entity.Role;

public record UserResponse(
        Long userId,
        String name,
        String phone,
        String email,
        Role role,
        String status
) {}