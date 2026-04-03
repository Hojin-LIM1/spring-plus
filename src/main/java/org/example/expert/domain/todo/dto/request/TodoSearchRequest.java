package org.example.expert.domain.todo.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


//내가 검색할 조건들을 만들어 줘야겠쥬?
@Getter
@Setter
@NoArgsConstructor
public class TodoSearchRequest {

    private String title;
    private String nickname;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}