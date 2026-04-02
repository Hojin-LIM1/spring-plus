package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {


    // : Todo -> TodoResponse로 쿄쿄
    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    Page<TodoResponse> findAllByOrderByModifiedAtDesc(Pageable pageable);  // user 기준 수정일

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN t.user " +
            "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId); //user


    // Todo로 받아서 처리할려고 했는데 TodoResponse로 받아도 상관 없을듯?
    @Query("select t from Todo t where t.weather = :weather and t.modifiedAt between :start and :end") // 날씨랑 날짜 검색 조건이 있을 때
    Page<TodoResponse> findAllByWeatherAndDate(String weather, LocalDateTime start, LocalDateTime end, Pageable pageable);

    // 날씨만 있을 때 : Todo -> TodoResponse로 쿄쿄
    Page<TodoResponse> findAllByWeather(String weather, Pageable pageable);

    // 기간만 있을 때 : Todo -> TodoResponse로 쿄쿄
    @Query("select t from Todo t where t.modifiedAt between :start and :end")
    Page<TodoResponse> findAllByModifiedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);


}
