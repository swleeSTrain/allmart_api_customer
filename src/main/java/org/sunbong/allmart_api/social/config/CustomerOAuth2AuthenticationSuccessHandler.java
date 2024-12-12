package org.sunbong.allmart_api.social.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.sunbong.allmart_api.security.util.JWTUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
@RequiredArgsConstructor
public class CustomerOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

     private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        log.info("OAuth2User attributes: {}", attributes);

        // 안전한 타입 확인 및 추출
//        Map<String, Object> kakaoAccount = null;
//        if (attributes.get("email") instanceof Map) {
//            kakaoAccount = (Map<String, Object>) attributes.get("email");
//        }

        // 이메일
        String email = attributes != null ? (String) attributes.get("email") : null;


        // JWT 생성 및 리다이렉트
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        String accessToken = jwtUtil.createToken(claims, 60);
        String refreshToken = jwtUtil.createToken(claims, 1440);

        String redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:5173/customer/update")
                .queryParam("email", email)
                .queryParam("accessToken", accessToken)
                .queryParam("accessToken", refreshToken)
                .build().toUriString();

        response.sendRedirect(redirectUrl);
    }



}
