package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CustomTodoRepository {
    Optional<Todo> findByIdWithUser(Long todoId);

    // 동적 쿼리기반 검색기능을만들어봅시둡
    // 메서드 추가해주구
    // 고려사항 : 제목, 생성일 최신순, 담당자 닉네임, 검색결과는 페이지로 처리해야되니 Pageable도 추가~
    // 검색조건 DTO 만들고 페이지처리 추가

    Page<TodoSearchResponse> searchTodos(TodoSearchRequest request, Pageable pageable);

}
