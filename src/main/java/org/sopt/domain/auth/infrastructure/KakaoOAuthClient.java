package org.sopt.domain.auth.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.auth.code.AuthErrorCode;
import org.sopt.domain.auth.infrastructure.dto.KakaoTokenResponse;
import org.sopt.domain.auth.infrastructure.dto.KakaoUserInfoResponse;
import org.sopt.global.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoOAuthClient {
    private static final String KAKAO_TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

    private final RestTemplate restTemplate;

    @Value("${oauth.kakao.client-id}")
    private String client;
    @Value("${oauth.kakao.client-secret}")
    private String clientSecret;
    @Value("${oauth.kakao.redirect-uri}")
    private String redirectUri;

    private final ObjectMapper objectMapper;

    // 카카오 OAuth Access 토큰 가져오기
    public String getKakaoAccessToken(String accessCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", accessCode);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        KakaoTokenResponse response = exchange(
                KAKAO_TOKEN_URI,
                HttpMethod.POST,
                kakaoTokenRequest,
                KakaoTokenResponse.class,
                AuthErrorCode.KAKAO_TOKEN_REQUEST_FAILED
        );

        if (response == null || response.accessToken() == null) {
            throw new CustomException(AuthErrorCode.KAKAO_INVALID_RESPONSE);
        }
        return response.accessToken();
    }

    public KakaoUserInfoResponse getKakaoUserInfo(String kakaoAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(kakaoAccessToken);

        HttpEntity<Void> kakaoProfileRequest = new HttpEntity<>(headers);

        KakaoUserInfoResponse response = exchange(
                KAKAO_USER_INFO_URI,
                HttpMethod.GET,
                kakaoProfileRequest,
                KakaoUserInfoResponse.class,
                AuthErrorCode.KAKAO_USER_INFO_REQUEST_FAILED
        );

        if (response == null || response.id() == null) {
            throw new CustomException(AuthErrorCode.KAKAO_INVALID_RESPONSE);
        }
        return response;
    }

    private <T> T exchange(
            String uri,
            HttpMethod method,
            HttpEntity<?> request,
            Class<T> responseType,
            AuthErrorCode errorCode
    ) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(uri, method, request, responseType);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // 카카오가 4xx/5xx 응답 (code 만료, 잘못된 client_id, 카카오 서버 장애 등)
            throw new CustomException(errorCode);
        } catch (RestClientException e) {
            // 네트워크 오류, 타임아웃, 역직렬화 실패 등
            throw new CustomException(errorCode);
        }
    }
}
