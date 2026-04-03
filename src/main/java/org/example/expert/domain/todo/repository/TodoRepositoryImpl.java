package org.example.expert.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.QTodoSearchResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;
import static org.example.expert.domain.manager.entity.QManager.manager; // 추가
import static org.example.expert.domain.comment.entity.QComment.comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryImpl implements CustomTodoRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {

        Todo result = queryFactory
                .selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    // 이제 검색에 반영할 쿼리문을 만들어야됨~
        // 고려사항 : 제목, 생성일 최신순, 담당자 닉네임, 검색결과는 페이지로 처리해야되니 Pageable도 추가~

        // 작업순서
        // 0. build했으니 Q클래스는 생략
        // 1. booleanBuilder로 동적쿼리 생성
        // 2. 담을 객체(컨테이너) 생성
    @Override
    public Page<TodoSearchResponse> searchTodos(TodoSearchRequest request,
                                                Pageable pageable){

        //1. * contains랑 날짜 between으로 설정해주기
        // 흐음 추후 검색조건 바꿀려면 빌더랑 request만 바꿔줍시당 : 시간되면 booleanExpression도 써보기
        BooleanBuilder builder = new BooleanBuilder();

        if(request.getTitle()!= null) builder.and(todo.title.contains((request.getTitle())));
        if(request.getNickname() != null) builder.and(manager.user.nickname.contains(request.getNickname()));
        if(request.getEndDate() != null && request.getStartDate() != null)
            builder.and(todo.createdAt.between(request.getStartDate(), request.getEndDate()));


        //2. content 빌드해주기 -> response에 담을거임
        // 제목, 매니저(id), 댓글(id) 넣어줄건데
        // select, countDistinct, from, leftjoin, where, groupBy, orderBy, offset.limit -> 이거 다 써야함.
        // select <- Q클래스 리스폰스에 담아줘야함 근데 중복값은 허용하지 않아(countDistinct)
        // from <- 당연히 일정을 기준으로
        // leftjoin <- 일정기준으로 담당자와 댓글을 왼쪽으로 몰거임
        // where <- booleanBuilder에 있는 검색조건으로 조립할거임
        // GroupBy <- 일정 id별로 묶을거임
        // orderBy <- 생성일 기준으로 내림차순 정렬
        // offset().limit() <- 몇번째 페이지부터 몇개 가져올지 (강의 참고)
        // fetch <- 결과 내놔
        List<TodoSearchResponse> content = queryFactory
                .select(new QTodoSearchResponse(
                        todo.title,
                        manager.id.countDistinct(),
                        comment.id.countDistinct()
                ))
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(todo.comments, comment)
                .where(builder)
                .groupBy(todo.id)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 3.최종 합계

        Long total = queryFactory
                .select(todo.count())
                .from(todo)
                .where(builder)
                .fetchOne();

        //4.내용이랑 페이지 가져오는데 null일 경우에 그냥 0으로 처리해버리기
        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }

}
