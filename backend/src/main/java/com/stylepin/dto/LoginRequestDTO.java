package com.stylepin.dto;
import jakarta.validation.constraints.*;
public record LoginRequestDTO(@NotBlank @Email @Size(max = 254) String email,
                              @NotBlank @Size(max = 72) String password) {
    @Override public String toString() { return "LoginRequestDTO[redacted]"; }
}
