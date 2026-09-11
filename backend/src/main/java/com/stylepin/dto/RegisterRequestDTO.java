package com.stylepin.dto;
import jakarta.validation.constraints.*;
public record RegisterRequestDTO(
    @NotBlank @Pattern(regexp = "[a-zA-Z0-9_]{3,30}", message = "Use 3–30 letters, numbers or underscores") String username,
    @NotBlank @Email @Size(max = 254) String email,
    @NotBlank @Size(min = 12, max = 72) String password) {
    @Override public String toString() { return "RegisterRequestDTO[redacted]"; }
}
