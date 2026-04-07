package org.example.expert.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public String createPresignedUrl(String fileName) {
        // 1. 파일 이름 중복 방지를 위해 UUID 추가
        String uniqueFileName = UUID.randomUUID() + "_" + fileName;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key("profiles/" + uniqueFileName) // S3 내부 경로
                .contentType("image/png")          // 필요시 확장자에 따라 동적 설정 가능
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10)) // 유효시간 10분
                .putObjectRequest(putObjectRequest)
                .build();

        return s3Presigner.presignPutObject(presignRequest).url().toString();
    }

    public String getPublicUrl(String uniqueFileName) {
        // 실제 S3 객체의 공개 주소 형식: https://버킷명.s3.리전.amazonaws.com/경로/파일명
        return String.format("https://%s.s3.%s.amazonaws.com/profiles/%s", bucket, region, uniqueFileName);
    }
}