package org.example.expert.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.S3Service;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;

    public UserResponse getUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new InvalidRequestException("User not found"));
        return new UserResponse(user.getId(), user.getEmail());
    }

    @Transactional
    public void changePassword(long userId, UserChangePasswordRequest userChangePasswordRequest) {
        validateNewPassword(userChangePasswordRequest);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        if (passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new InvalidRequestException("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
        }

        if (!passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new InvalidRequestException("잘못된 비밀번호입니다.");
        }

        user.changePassword(passwordEncoder.encode(userChangePasswordRequest.getNewPassword()));
    }

    private static void validateNewPassword(UserChangePasswordRequest userChangePasswordRequest) {
        if (userChangePasswordRequest.getNewPassword().length() < 8 ||
                !userChangePasswordRequest.getNewPassword().matches(".*\\d.*") ||
                !userChangePasswordRequest.getNewPassword().matches(".*[A-Z].*")) {
            throw new InvalidRequestException("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.");
        }
    }


    @Transactional
    public void updateProfileImage(Long userId, String fileName) {
        // 1. 유저 찾기 (없으면 에러 던지기)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        // 2. S3의 최종 객체 URL 생성 (버킷명, 리전, 경로 조합)
        // 예: https://hojin-bucket.s3.ap-northeast-2.amazonaws.com/profiles/UUID_test.png
        String s3Url = s3Service.getPublicUrl(fileName);

        // 3. 유저 엔티티의 필드 업데이트
        user.updateProfileImage(s3Url);

        // @Transactional이 붙어있으므로 별도의 save 호출 없이도 DB에 반영됩니다! (더티 체킹)
    }




    public UserResponse findUserByNickname(String nickname) {
        long startTime = System.currentTimeMillis();

        // JPA 기본 findByNickname 호출 (인덱스 없는 상태)
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("없음"));

        long endTime = System.currentTimeMillis();
        System.out.println("검색 소요 시간: " + (endTime - startTime) + "ms");
        return null;
    }
}
