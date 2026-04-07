package org.example.expert.domain;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/health")
    public String health() {
        return "ok"; // 외부에서 접속했을 때 "ok"가 보이면 성공!
    }
}
