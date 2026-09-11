package com.stylepin.dto;
public record AuthResponseDTO(String accessToken, String tokenType, long expiresIn, UserResponseDTO user) {
    @Override public String toString() { return "AuthResponseDTO[redacted]"; }
}
