package org.example.expert.domain.todo.dto.response;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TodoSearchResponse {

    //Entity를 대신할 dto먼저 작성해버리기
    // 반환할 검색 결과 : 제목, 담당자수, 댓글 수

    private String title;
    private Long managerCount;
    private Long commentCount;

    @QueryProjection
    public TodoSearchResponse(String title, Long managerCount, Long commentCount) {
        this.title = title;
        this.managerCount = managerCount;
        this.commentCount = commentCount;
    }

}
