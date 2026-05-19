package org.sopt.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.auth.code.AuthSuccessCode;
import org.sopt.domain.auth.dto.response.TokenResponse;
import org.sopt.domain.auth.service.AuthService;
import org.sopt.global.common.response.SuccessResponse;
import org.sopt.domain.user.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인 (Access Token + Refresh Token 발급)")
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<TokenResponse>> login(
            @RequestParam("email") String email,
            @RequestParam("password") String password
    ) {
        TokenResponse tokens = authService.login(email, password);

        return ResponseEntity.ok(SuccessResponse.of(AuthSuccessCode.TOKEN_ISSUE_SUCCESS, tokens));
    }

    @Operation(summary = "내 정보 조회 (Access Token 검증)")
    @GetMapping("/me")
    public ResponseEntity<SuccessResponse<UserResponse>> me(Authentication authentication) {

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalArgumentException("인증되지 않았습니다.");
        }

        Long userId = Long.parseLong(authentication.getName());
        UserResponse user = authService.getUserById(userId);

        return ResponseEntity.ok(SuccessResponse.of(AuthSuccessCode.GET_MY_INFO_SUCCESS, user));
    }
}
