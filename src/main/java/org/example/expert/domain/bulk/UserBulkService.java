package org.example.expert.domain.bulk;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBulkService {

    private final UserBulkRepository userBulkRepository;

    public void createFiveMillionUsers() {
        int totalCount = 5_000_000;
        int batchSize = 10_000; // 1만 건씩 끊어서 처리

        List<User> users = new ArrayList<>();

        for (int i = 1; i <= totalCount; i++) {
            // 중복 방지를 위해 숫자 기반 닉네임 생성
            User user = User.builder()
                    .nickname("User_" + i)
                    .password("password123")
                    .email(i + "@example.com")
                    .userRole(UserRole.USER)
                    .build();

            users.add(user);

            // 1만 건이 쌓이면 DB에 쏘고 리스트 비우기
            if (i % batchSize == 0) {
                userBulkRepository.bulkInsert(users);
                users.clear();
                System.out.println(i + "건 완료...");
            }
        }
    }
}
