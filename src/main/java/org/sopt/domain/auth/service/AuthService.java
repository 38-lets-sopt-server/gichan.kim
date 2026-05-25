package org.sopt.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.auth.code.AuthErrorCode;
import org.sopt.domain.auth.dto.request.LoginRequest;
import org.sopt.domain.auth.dto.request.SignUpRequest;
import org.sopt.domain.auth.infrastructure.KakaoOAuthClient;
import org.sopt.domain.auth.infrastructure.dto.KakaoUserInfoResponse;
import org.sopt.domain.auth.service.dto.LoginResult;
import org.sopt.domain.user.entity.AuthProvider;
import org.sopt.domain.user.exception.UserErrorCode;
import org.sopt.global.exception.CustomException;
import org.sopt.global.jwt.JwtProvider;
import org.sopt.domain.auth.entity.RefreshToken;
import org.sopt.domain.auth.repository.RefreshTokenRepository;
import org.sopt.domain.auth.dto.response.TokenResponse;
import org.sopt.domain.user.entity.User;
import org.sopt.domain.user.dto.response.UserResponse;
import org.sopt.domain.user.repository.UserRepository;
import org.sopt.global.jwt.service.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;
    private final KakaoOAuthClient kakaoOAuthClient;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlacklistService tokenBlacklistService;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${security.jwt.refresh-token-expires-in-seconds:1209600}")
    private long refreshTokenExpiresInSeconds;

    @Transactional
    public LoginResult signUp(SignUpRequest request){
        if (userRepository.existsByEmail(request.email())){
            throw new CustomException(AuthErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = userRepository.save(
                User.createLocalUser(
                        request.email(),
                        passwordEncoder.encode(request.password()),
                        request.nickname())
        );

        TokenResponse tokens = issueTokens(user);

        return new LoginResult(
                tokens.accessToken(),
                tokens.refreshToken(),
                user);
    }

    @Transactional
    public LoginResult login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .filter(u -> u.getProvider() == AuthProvider.LOCAL)
                .orElseThrow(() -> new CustomException(AuthErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new CustomException(AuthErrorCode.INVALID_CREDENTIALS);
        }

        TokenResponse tokens = issueTokens(user);

        redisTemplate.opsForValue().set("TEST_KEY", "HELLO", 60, TimeUnit.SECONDS);

        return new LoginResult(
                tokens.accessToken(),
                tokens.refreshToken(),
                user);
    }

    @Transactional
    public LoginResult loginWithKakao(String accessCode) {
        String oAuthAccessToken = kakaoOAuthClient.getKakaoAccessToken(accessCode);
        KakaoUserInfoResponse userInfo = kakaoOAuthClient.getKakaoUserInfo(oAuthAccessToken);
        String kakaoUserId = String.valueOf(userInfo.id());

        User user = userRepository.findByProviderAndProviderId(AuthProvider.KAKAO, kakaoUserId)
                .orElseGet(() -> registerKakaoUser(kakaoUserId, userInfo));

        TokenResponse tokens = issueTokens(user);

        return new LoginResult(
                tokens.accessToken(),
                tokens.refreshToken(),
                user);
    }

    private User registerKakaoUser(String kakaoUserId, KakaoUserInfoResponse userInfo) {
        String email = userInfo.kakaoAccount().email();
        String nickname = userInfo.kakaoAccount().profile().nickname();

        return userRepository.save(
                User.createKakaoUser(kakaoUserId, email, nickname)
        );
    }

    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        return UserResponse.from(user);
    }

    private TokenResponse issueTokens(User user) {
        String accessToken = jwtProvider.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId());

        refreshTokenRepository.findByUserId(user.getId())
                .ifPresentOrElse(
                        existing -> existing.rotate(refreshToken, refreshTokenExpiresInSeconds),
                        () -> refreshTokenRepository.save(
                                RefreshToken.of(user.getId(), refreshToken, refreshTokenExpiresInSeconds)
                        )
                );

        return TokenResponse.of(accessToken, refreshToken);
    }

    @Transactional
    public TokenResponse reissue(String refreshTokenValue) {
        RefreshToken stored = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new CustomException(AuthErrorCode.INVALID_REFRESH_TOKEN));

        if (!stored.getToken().equals(refreshTokenValue)) {
            refreshTokenRepository.deleteByToken((refreshTokenValue));
            throw new CustomException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (stored.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(AuthErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        User user = userRepository.findById(stored.getUserId())
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        return issueTokens(user);
    }


    @Transactional
    public void logout(String accessTokenValue, String refreshTokenValue) {
        refreshTokenRepository.findByToken(refreshTokenValue)
                .ifPresent(refreshTokenRepository::delete);

        long remaining = jwtProvider.getRemainingExpiration(accessTokenValue);

        if (remaining > 0) {
            tokenBlacklistService.addToBlacklist(accessTokenValue, remaining);
        }
    }
}
