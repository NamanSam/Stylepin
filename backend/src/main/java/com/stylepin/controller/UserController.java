package com.stylepin.controller;
import com.stylepin.dto.UserResponseDTO;
import com.stylepin.service.CurrentUserService;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/users/me")
public class UserController {
    private final CurrentUserService current;
    public UserController(CurrentUserService current) { this.current = current; }
    @GetMapping public UserResponseDTO me() { return UserResponseDTO.from(current.get()); }
}
