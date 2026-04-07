package org.example.expert.domain.log;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogService {
    private final LogRepository logRepository;


    // 흐음 요약하자면 매니저 등록 성공 실패 여부 상관없이 난 로그를 남기겠다는 내용
    // 부모 트랜잭션으로부터 독립한다!
    // 독립상태로 log작업은 무조건 db에 저장하겠다
    // 이거 쓸라믄 자기호출 문제때문에 무조건 서로 다른 클래스에서 호출해야함.
    @Transactional(propagation = Propagation.REQUIRES_NEW) // 여기가 핵심!
    public void saveLog(String message) {
        logRepository.save(new Log(message));
    }
}
