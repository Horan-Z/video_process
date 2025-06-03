package com.vp.vpbackend.controller;

import com.vp.vpbackend.POJO.DTO.AuthDTO;
import com.vp.vpbackend.POJO.DTO.TokenDTO;
import com.vp.vpbackend.POJO.Result;
import com.vp.vpbackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result login(@RequestBody AuthDTO authDTO) {
        return authService.login(authDTO);
    }

    @PostMapping("/register")
    public Result register(@RequestBody AuthDTO authDTO) {
        return authService.register(authDTO);
    }

    @PostMapping("/logout")
    public Result logout(@RequestBody TokenDTO tokenDTO) {
        return authService.logout(tokenDTO);
    }

    @PostMapping("check-login")
    public Result checkLogin() {
        return authService.checkLogin();
    }
}
