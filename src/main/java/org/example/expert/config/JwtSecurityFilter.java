package org.example.expert.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


//JWT 필터를 다시 만들어보자
// 중복실행이 되지 않도록 OncePerRequestFilter를 상속받아주구
// 토큰 뽑아내주구
// 클레임 데이터 뽑아주구
// Spring Security 인증 객체 생성하고 저장해주구
// 필터 체인 유지해주구
// 컨트롤러 연결해주면 오와리다!
@Slf4j
@RequiredArgsConstructor
public class JwtSecurityFilter extends OncePerRequestFilter {

    //1. jwtUtil 불러와!
    private final JwtUtil jwtUtil;


    //2. 요청, 응답, 필터
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //3. 헤더에 오토리제이션을 내놓으시용, JWT 토큰내놔
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = jwtUtil.substringToken(bearerToken);

            //4. 유틸로 토큰을 검증하고~ 토큰 만들때 넣었던 4가지 정보를 변수로 설정해~
            try {
                Claims claims = jwtUtil.extractClaims(token);
                Long userId = Long.parseLong(claims.getSubject());
                String email = claims.get("email", String.class);
                UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class));
                String nickname = claims.get("nickname",String.class);  // nickname 추가해봤어염

                // 5. 여기서 작업해둔 SpringSecurity 인증(User) 객체 만들고 ContextHolder에 저장
                UserDetailsImpl userDetails = new UserDetailsImpl(userId, email, userRole, nickname);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                log.error("JWT 인증 실패", e);
            }
        }


        //6. 토큰검사 끝났나염? -> 필터 작동 -> 다음 단계로 넘어가기~

        filterChain.doFilter(request, response);
    }
}