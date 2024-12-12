    package org.sunbong.allmart_api.security.config;

    import lombok.RequiredArgsConstructor;
    import lombok.extern.log4j.Log4j2;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
    import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
    import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.AuthenticationFailureHandler;
    import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
    import org.springframework.web.cors.CorsConfiguration;
    import org.springframework.web.cors.CorsConfigurationSource;
    import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
    import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
    import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
    import org.sunbong.allmart_api.social.config.CustomAuthenticationFailureHandler;
    import org.sunbong.allmart_api.social.config.CustomAuthenticationSuccessHandler;
    import org.sunbong.allmart_api.social.service.CustomOAuth2UserService;

    import java.util.List;

    @Log4j2
    @Configuration
    @EnableMethodSecurity(prePostEnabled = true)
    @RequiredArgsConstructor
    public class CustomSecurityConfig implements WebMvcConfigurer {

        private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

        private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

        private final CustomOAuth2UserService customOAuth2UserService;

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

            http.formLogin(config -> config.disable());

            http.sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.NEVER));

            http.csrf(config -> config.disable());

    //        http.addFilterBefore(new JWTCheckFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

            http.cors(cors -> cors.configurationSource(corsConfigurationSource()));




            // 카카오톡 로그인 엔드포인트 설정
            http.oauth2Login(oauth2 -> oauth2
                    .loginPage("http://localhost:5173/customer/signIn/") // 사용자 정의 로그인 페이지 설정 (필요하면 변경)"
                    .successHandler(customAuthenticationSuccessHandler)
                    .failureHandler(customAuthenticationFailureHandler)
                    .authorizationEndpoint(auth -> auth
                            .baseUri("/oauth2/authorization")) // 기본 OAuth2 인증 URL
                    .tokenEndpoint(token -> token
                            .accessTokenResponseClient(customAccessTokenResponseClient())) // Access Token 처리
                    .userInfoEndpoint(userInfo  -> userInfo
                            .userService(customOAuth2UserService) // 사용자 정보 처리 서비스

                    ));

//            http.authorizeHttpRequests(authorize -> authorize
//                    .requestMatchers("/api/v1/customer/makeToken","/api/v1/customer/signUp/phoneNumber/**",
//                            "/api/v1/qrcode/signUp", "/api/v1/customer/signIn", "/api/v1/customer/**","/customer/**","/login/**","/oauth2/**", "/customer/signin","/eror").permitAll()
//                    .requestMatchers("/api/v1/**").hasRole("USER") // /api/v1/** 경로는 관리자 권한만 접근 가능
//
//
//                    .anyRequest().authenticated()
//            );

            http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

    //        http.authorizeHttpRequests(authorize -> authorize
    //                .requestMatchers("/uploads/**").permitAll() // /uploads/** 경로 허용
    //                .requestMatchers("/api/v1/member/signUp", "/api/v1/member/makeToken",
    //                        "api/v1/mart/**").permitAll()
    //                .requestMatchers("/api/v1/**").hasRole("MARTADMIN") // /api/v1/** 경로는 관리자 권한만 접근 가능
    //                .anyRequest().authenticated()
    //        );

            return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {

            CorsConfiguration corsConfiguration = new CorsConfiguration();

            corsConfiguration.setAllowedOriginPatterns(List.of("*")); // 어디서든 허락
            corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"));
            corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
            corsConfiguration.setAllowCredentials(true);

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", corsConfiguration);

            return source;
        }

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            // 이미지 요청을 처리할 경로
            registry.addResourceHandler("/uploads/**")
                    .addResourceLocations("file:///C:/upload/"); // 실제 업로드 경로
        }


        @Bean
        public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> customAccessTokenResponseClient() {
            return new DefaultAuthorizationCodeTokenResponseClient();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
            return config.getAuthenticationManager();
        }





    }

