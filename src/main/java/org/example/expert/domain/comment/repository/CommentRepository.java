package org.example.expert.domain.comment.repository;

import org.example.expert.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    //로그 정보를 보니 comment를 조회할때
    //음.. 그냥 join을 붙이면 user 객체안에 해당하는 정보들이 비어있는 체로 넘어오면서 그 비어있는걸 조회할려고 db무한왕복해야되서 n+1이 발생함.
    // fetch만 붙여줌 ㅇㅇ
    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.todo.id = :todoId")
    List<Comment> findByTodoIdWithUser(@Param("todoId") Long todoId);
}
