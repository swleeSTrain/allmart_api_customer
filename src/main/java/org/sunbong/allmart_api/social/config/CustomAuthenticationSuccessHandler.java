package org.sunbong.allmart_api.social.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.sunbong.allmart_api.social.dto.SocialCustomerDTO;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        if (authentication.getPrincipal() instanceof SocialCustomerDTO) {
            SocialCustomerDTO customerDTO = (SocialCustomerDTO) authentication.getPrincipal();

            // JSON 응답으로 사용자 정보 반환
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(customerDTO));
            response.sendRedirect("http://localhost:5173/");
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
        }

    }


//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                        Authentication authentication) throws IOException {
//        if (authentication.getPrincipal() instanceof SocialCustomerDTO) {
//            SocialCustomerDTO customerDTO = (SocialCustomerDTO) authentication.getPrincipal();
//
//            // Access Token, Refresh Token 포함하여 React로 리디렉션
//            String redirectUrl = "http://localhost:5173/?accessToken=" + customerDTO.getAccessToken()
//                    + "&refreshToken=" + customerDTO.getRefreshToken()
//                    + "&name=" + customerDTO.getName()
//                    + "&email=" + customerDTO.getEmail();
//
//            response.sendRedirect(redirectUrl);
//        } else {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
//        }
//    }

}
