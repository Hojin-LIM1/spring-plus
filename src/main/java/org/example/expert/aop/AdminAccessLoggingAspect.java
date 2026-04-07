package org.example.expert.aop;  //check

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Aspect  // check
@Component  // check
@RequiredArgsConstructor
public class AdminAccessLoggingAspect {

    private final HttpServletRequest request;


    // after? around? before?
    // usercontroller에서 getuser 메서드 이후에 동작
//    @After("execution(* org.example.expert.domain.user.controller.UserController.getUser(..))")
    //UserAdminController 클래스의 changeUserRole() 메소드가 실행 전 동작해야해요 <- 요구사항을 충족시키기 위해
    // 실행 전이므로 before로 바꿔주고 경로(UserAdminController.changeUserRole)도 바꿔줘야 할 듯?
    @Before("execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    //메서드명도 바꿈 after -> before
    public void logBeforeChangeUserRole(JoinPoint joinPoint) {
        String userId = String.valueOf(request.getAttribute("userId"));
        String requestUrl = request.getRequestURI();
        LocalDateTime requestTime = LocalDateTime.now();

        log.info("Admin Access Log - User ID: {}, Request Time: {}, Request URL: {}, Method: {}",
                userId, requestTime, requestUrl, joinPoint.getSignature().getName());
    }
}
