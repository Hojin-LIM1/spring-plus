package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

//커스텀 레포 상속시켜주기
public interface TodoRepository extends JpaRepository<Todo, Long>, CustomTodoRepository {

    //
    @Query("select t from Todo t " +
            // 날씨 조건 +
            "where (:weather is null or t.weather = :weather) " +
            // 수정일 기준 시작일보다 크고 종료일보다 작은
            "and (:startDate is null or t.modifiedAt >= :startDate) " +
            "and (:endDate is null or t.modifiedAt <= :endDate) " +
            // 수정일 내림차순으로
            "order by t.modifiedAt desc")
    Page<Todo> findByCondition(
            // 검토 결과 @Param을 추가하게 됬는데 @Param을 사용하면 JPQL변수와 파라미터 이름이 다르더라고 @Param으로 보완 가능 :D
            @Param("weather") String weather,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);




//    // : Todo -> TodoResponse로 쿄쿄 -> if문도 길어지므로 JPQL문 하나로 써보기
//    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
//    Page<TodoResponse> findAllByOrderByModifiedAtDesc(Pageable pageable);  // user 기준 수정일


    //QueryDSL로 삭제
//    @Query("SELECT t FROM Todo t " +
//            "LEFT JOIN t.user " +
//            "WHERE t.id = :todoId")
//    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId); //user


//    // Todo로 받아서 처리할려고 했는데 TodoResponse로 받아도 상관 없을듯? -> if문도 길어지므로 JPQL문 하나로 써보기
//    @Query("select t from Todo t where t.weather = :weather and t.modifiedAt between :start and :end") // 날씨랑 날짜 검색 조건이 있을 때
//    Page<TodoResponse> findAllByWeatherAndDate(String weather, LocalDateTime start, LocalDateTime end, Pageable pageable);
//
//    // 날씨만 있을 때 : Todo -> TodoResponse로 쿄쿄 -> if문도 길어지므로 JPQL문 하나로 써보기
//    Page<TodoResponse> findAllByWeather(String weather, Pageable pageable);
//
//    // 기간만 있을 때 : Todo -> TodoResponse로 쿄쿄 -> if문도 길어지므로 JPQL문 하나로 써보기
//    @Query("select t from Todo t where t.modifiedAt between :start and :end")
//    Page<TodoResponse> findAllByModifiedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);


}
