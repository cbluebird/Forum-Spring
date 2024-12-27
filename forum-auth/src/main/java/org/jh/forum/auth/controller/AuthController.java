package org.jh.forum.auth.controller;

import org.jh.forum.auth.dto.UserDTO;
import org.jh.forum.auth.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private IAuthService authService;

    @PostMapping("/login")
    public void login(@RequestBody @Validated UserDTO userDTO) {
        authService.login(userDTO);
    }
}