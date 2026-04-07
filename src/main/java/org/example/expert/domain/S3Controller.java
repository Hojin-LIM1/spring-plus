package org.example.expert.domain;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class S3Controller {

    private final S3Service s3Service;
    private final UserService userService;

    /**
     * 1. 업로드용 Presigned URL 발급 API
     * @param fileName 확장자를 포함한 파일명 (예: "my_profile.png")
     */
    @GetMapping("/profile/presigned-url")
    public ResponseEntity<String> getPresignedUrl(@RequestParam String fileName) {
        // 서비스에서 1회용 업로드 주소를 생성해서 반환
        String presignedUrl = s3Service.createPresignedUrl(fileName);
        return ResponseEntity.ok(presignedUrl);
    }

    /**
     * 2. 업로드 완료 후 DB에 이미지 경로 저장 API
     * (유저가 S3에 파일을 직접 올린 뒤, 이 API를 호출해서 "나 다 올렸어!"라고 알려주는 용도)
     */
    @PatchMapping("/profile/image")
    public ResponseEntity<Void> updateProfileImage(
            @AuthenticationPrincipal AuthUser authUser, // 로그인한 유저 정보
            @RequestParam String fileName // S3에 저장된 파일 이름
    ) {
        // DB의 User 테이블에 S3 주소를 업데이트하는 로직
        userService.updateProfileImage(authUser.getId(), fileName);
        return ResponseEntity.ok().build();
    }
}
