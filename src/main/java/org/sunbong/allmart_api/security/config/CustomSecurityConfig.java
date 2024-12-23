    package org.sunbong.allmart_api.security.config;

    import lombok.RequiredArgsConstructor;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
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
    import org.springframework.web.cors.CorsConfiguration;
    import org.springframework.web.cors.CorsConfigurationSource;
    import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
    import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
    import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
    import org.sunbong.allmart_api.social.config.CustomerOAuth2AuthenticationSuccessHandler;
    import org.sunbong.allmart_api.social.service.CustomOAuth2UserService;

    import java.util.List;

    @Configuration
    @EnableMethodSecurity(prePostEnabled = true)
    @RequiredArgsConstructor
    public class CustomSecurityConfig implements WebMvcConfigurer {


        private final AuthenticationFailureHandler customAuthenticationFailureHandler;

        private final CustomOAuth2UserService customOAuth2UserService;

        private final CustomerOAuth2AuthenticationSuccessHandler customerOAuth2AuthenticationSuccessHandler;

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService) throws Exception {

            http.formLogin(config -> config.disable());

            http.sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.NEVER));

            http.csrf(config -> config.disable());

    //        http.addFilterBefore(new JWTCheckFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

            http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

            // 카카오톡 로그인 엔드포인트 설정
            http.oauth2Login(oauth2 -> oauth2
                    .loginPage("http://localhost:5173/customer/signin") // 사용자 정의 로그인 페이지 설정 (필요하면 변경)"
                    //.loginPage("/api/v1/customer")
                    //.defaultSuccessUrl("http://localhost:5173", true) // 로그인 성공 시 이동할 URL
                    //.successHandler(customerOAuth2AuthenticationSuccessHandler)
                    .successHandler((request, response, authentication) -> {
                        // 인증 성공 시 React로 리디렉션
                        String redirectUrl = "http://localhost:5173/oauth/kakao";
                        response.sendRedirect(redirectUrl);
                    })
                    //.defaultSuccessUrl("/api/v1/customer/signIn", true) // 로그인 성공 시 이동할 URL
                    //.failureUrl("http://localhost:5173/login/failure") // 로그인 실패 시 이동할 URL
                    .failureHandler(customAuthenticationFailureHandler)
                    .authorizationEndpoint(auth -> auth
                            .baseUri("/oauth2/authorization")) // 기본 OAuth2 인증 URL
                    .tokenEndpoint(token -> token
                            .accessTokenResponseClient(customAccessTokenResponseClient  ())) // Access Token 처리
                    .userInfoEndpoint(userInfo  -> userInfo
                           .userService(customOAuth2UserService) // 사용자 정보 처리 서비스

                    ));
            //
//            http.oauth2Login(oauth -> oauth
//                    .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService)));

//              http.oauth2Login(oauth -> oauth
//                              .successHandler((request, response, authentication) -> {
//                                  // 인증 성공 후 프론트엔드로 리다이렉트
//                                  response.sendRedirect("http://localhost:5173/oauth/kakao");
//                              }));

//            http.authorizeHttpRequests(authorize -> authorize
//                    .requestMatchers("/api/v1/customer/makeToken","/api/v1/customer/signUp/phoneNumber/**",
//                            "/api/v1/qrcode/signUp", "/api/v1/customer/signIn", "/api/v1/customer/**","/customer/**","/login/**","/oauth2/**", "/customer/signin","/eror").permitAll()
//                    .requestMatchers("/api/v1/**").hasRole("USER") // /api/v1/** 경로는 관리자 권한만 접근 가능
//x x
//
//                    .anyRequest().authenticated()
//            );

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

            //corsConfiguration.setAllowedOriginPatterns(List.of("http://localhost:5173")); // 어디서든 허락
            corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173"));
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


    }
