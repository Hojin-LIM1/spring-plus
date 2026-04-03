package org.example.expert.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {


    //UserDetail 만들었고, JwtSecurityFilter 만들었고 이제 FilterConfig, WebConfig, jwtFilter 다 합쳐버려~
    //필터체인을 만들거임

    private final JwtUtil jwtUtil;

    // 1. csrf는 잠시 꺼둬~ 우리는 뉴진스 쿠키가 아닌 jwt토큰을 쓸거니께루
    // 2. 토큰만 쓸거라 세션도 쓰지 않을거니 무상태로 둬버리고
    // 3. URL 에다가 접근권한 설정할거임
    //  가. 회원가입은 로그인 누구나 가능  나. admin이 있으니 admin 페이지 갈때 권한 체크 다. 그외는 로그인해야 할 수 있음으로 처리할게염
    // 4. 정수기 맹키로 필터 갈아껴주기~
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                //1.
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                //2.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 미사용
                //3. - 가,나,다
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // 회원가입, 로그인 허용
                        .requestMatchers("/admin/**").hasRole("ADMIN") // 관리자 권한 체크
                        .anyRequest().authenticated()
                )
                //4.
                .addFilterBefore(new JwtSecurityFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .build();


        // 아 이제 컨트롤러 가서 @AuthenticationPrincipal 어노테이션으로 스프링 시큐리티 방식으로 바꿔야함
        // authUser 수정도 해야함
    }
}
