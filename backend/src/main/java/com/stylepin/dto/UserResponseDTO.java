package com.stylepin.dto;
import com.stylepin.entity.User;
public record UserResponseDTO(Long id, String username, String email) {
    public static UserResponseDTO from(User user) {
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
    }
}
