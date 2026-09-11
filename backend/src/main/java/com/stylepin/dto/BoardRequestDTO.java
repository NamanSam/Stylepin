package com.stylepin.dto;
import jakarta.validation.constraints.*;
public record BoardRequestDTO(@NotBlank @Size(max=100) String name,@Size(max=1000) String description){}
