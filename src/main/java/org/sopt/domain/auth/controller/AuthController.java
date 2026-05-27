package org.sopt.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.auth.code.AuthSuccessCode;
import org.sopt.domain.auth.dto.request.LoginRequest;
import org.sopt.domain.auth.dto.request.SignUpRequest;
import org.sopt.domain.auth.dto.response.KakaoLoginResponse;
import org.sopt.domain.auth.dto.response.LoginResponse;
import org.sopt.domain.auth.dto.response.TokenResponse;
import org.sopt.domain.auth.service.AuthService;
import org.sopt.domain.auth.service.dto.LoginResult;
import org.sopt.global.common.response.SuccessResponse;
import org.sopt.global.jwt.CookieUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입 (Access Token + Refresh Token 발급), 회원 가입 성공 시 로그인 처리")
    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse<LoginResponse>> signUp(
            @Valid @RequestBody SignUpRequest request
    ) {
        LoginResult result = authService.signUp(request);

        ResponseCookie refreshTokenCookie = CookieUtil.createRefreshToken(result.refreshToken(), 1209600);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + result.accessToken())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(SuccessResponse.of(AuthSuccessCode.SIGN_UP_SUCCESS, LoginResponse.from(result)));
    }

    @Operation(summary = "로그인 (Access Token + Refresh Token 발급)")
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        LoginResult result = authService.login(request);

        ResponseCookie refreshTokenCookie = CookieUtil.createRefreshToken(result.refreshToken(), 1209600);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + result.accessToken())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(SuccessResponse.of(AuthSuccessCode.LOCAL_LOGIN_SUCCESS, LoginResponse.from(result)));
    }

    @Operation(summary = "카카오 로그인 (Access Token + Refresh Token 발급), 미 회원가입 시 회원가입 처리")
    @GetMapping("/kakao/login")
    public ResponseEntity<SuccessResponse<KakaoLoginResponse>> loginWithKakao(
            @RequestParam("code") String accessCode
    ){
        LoginResult result = authService.loginWithKakao(accessCode);
        ResponseCookie refreshTokenCookie = CookieUtil.createRefreshToken(result.refreshToken(), 1209600);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + result.accessToken())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(SuccessResponse.of(AuthSuccessCode.KAKAO_LOGIN_SUCCESS, KakaoLoginResponse.from(result)));
    }

    @Operation(
            summary = "refresh token 재발급",
            description = "refresh token을 검증한 후 새로운 access token을 재발급하며, 필요 시 refresh token도 함께 갱신"
    )
    @PostMapping("/reissue")
    public ResponseEntity<SuccessResponse<Void>> reissue(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        TokenResponse tokens = authService.reissue(refreshToken);

        ResponseCookie refreshTokenCookie = CookieUtil.createRefreshToken(tokens.refreshToken(), 60 * 60 * 24 * 7);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.accessToken())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(SuccessResponse.of(AuthSuccessCode.REFRESH_TOKEN_REISSUE_SUCCESS));
    }

    @Operation(
            summary = "로그아웃",
            description = "Refresh Token을 삭제하고 쿠키를 만료시켜 로그아웃 처리"
    )
    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse<Void>> logout(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        String accessToken = extractToken(authHeader);
        authService.logout(accessToken, refreshToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, CookieUtil.deleteRefreshToken().toString())
                .body(SuccessResponse.of(AuthSuccessCode.LOGOUT_SUCCESS));
    }

    public String extractToken(String bearer) {
        if (bearer == null || !bearer.startsWith("Bearer ")) return null;
        return bearer.substring(7);
    }
}
