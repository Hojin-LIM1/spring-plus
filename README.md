# SPRING PLUS
# ec2 생성<img width="1484" height="985" alt="ec2 생성완료" src="https://github.com/user-attachments/assets/e35358f5-3464-479f-ace2-2a8a6a31e0c5" />
# db생성<img width="1832" height="494" alt="db 생성완료" src="https://github.com/user-attachments/assets/1e3d3ac9-0cd3-417a-9ce7-b8e61f0a55cd" />
# ec2 & db 연결<img width="721" height="461" alt="서버 연결" src="https://github.com/user-attachments/assets/47f81fd6-7fe8-4f57-9c51-da3f86272846" />
# ec2 구동<img width="902" height="1010" alt="구동완료" src="https://github.com/user-attachments/assets/845d2264-6e9f-4e6b-a95d-6b4c22272d6d" />

# s3 버킷 생성<img width="1865" height="960" alt="s3 버킷 생성" src="https://github.com/user-attachments/assets/b9cc8a77-18dd-4fff-a000-5a9a3d6e364e" />


### 1. 대용량 데이터 삽입 (Bulk Insert)
- **사용 기술**: `JdbcTemplate` (Batch Update)
- **최적화 설정**: JDBC URL 옵션에 `rewriteBatchedStatements=true` 적용
- **데이터 양**: 5,000,000건
- **결과**: 총 **88초** 소요 (약 1분 28초)
- **분석**: JPA의 `saveAll` 방식(단건 호출) 대신 JDBC Batch를 활용하여 네트워크 오버헤드를 최소화함으로써 대량의 데이터를 매우 빠르게 삽입할 수 있었습니다.

---

### 2. 조회 성능 테스트 및 인덱스(Index) 최적화
유저 닉네임을 조건으로 하는 검색 기능에 대해 인덱스 적용 전/후 성능을 비교 측정했습니다.
- **검색 대상**: `User_4999999` (데이터의 최하단부 검색)

| 구분 | 검색 방식 | 소요 시간 | 비고 |
| :--- | :--- | :--- | :--- |
| **인덱스 적용 전** | **Full Table Scan** | **4,281ms** | 전체 행(500만건)을 하나씩 대조 |
| **인덱스 적용 후** | **Index Range Scan** | **210ms** | B-Tree 구조를 통해 즉시 접근 (약 20배 향상) |
