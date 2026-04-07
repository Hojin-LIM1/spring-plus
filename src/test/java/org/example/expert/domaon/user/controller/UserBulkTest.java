package org.example.expert.domaon.user.controller;


import org.example.expert.domain.bulk.UserBulkService;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserBulkTest {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private UserBulkService userBulkService;

    @Test
    void testBulkInsert() {
        long startTime = System.currentTimeMillis();

        userBulkService.createFiveMillionUsers();

        long endTime = System.currentTimeMillis();
        System.out.println("총 소요 시간: " + (endTime - startTime) / 1000 + "초");
    }

    @Test
    void 검색_성능_테스트() {
        String targetNickname = "User_4999999";

        long startTime = System.currentTimeMillis();

        userRepository.findByNickname(targetNickname);

        long endTime = System.currentTimeMillis();
        System.out.println("검색 소요 시간: " + (endTime - startTime) + "ms");
    }
}
