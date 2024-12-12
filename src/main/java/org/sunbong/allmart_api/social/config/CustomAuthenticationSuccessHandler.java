package org.sunbong.allmart_api.social.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.sunbong.allmart_api.social.dto.SocialCustomerDTO;

import java.io.IOException;

@Log4j2
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    public CustomAuthenticationSuccessHandler() {
        log.info("CustomAuthenticationSuccessHandler initialized"); // 초기화 로그
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.info("onAuthenticationSuccess called"); // 메서드 호출 로그
        if (authentication.getPrincipal() instanceof SocialCustomerDTO) {
            SocialCustomerDTO customerDTO = (SocialCustomerDTO) authentication.getPrincipal();

            ObjectMapper objectMapper = new ObjectMapper();
            String customerJson = objectMapper.writeValueAsString(customerDTO);

            log.info("Authentication successful. Customer info: {}", customerJson);

            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(customerJson);
        } else {
            log.error("Authentication failed: Principal is not of type SocialCustomerDTO");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
        }
    }
}


