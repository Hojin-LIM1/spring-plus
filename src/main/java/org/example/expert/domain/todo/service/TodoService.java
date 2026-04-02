package org.example.expert.domain.todo.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)  // <- 클래스 전체가 읽기 전용으로 되어 있어서 saveTodo에 @Transactional만 추가하면 되겠군
public class TodoService {

    private final TodoRepository todoRepository;
    private final WeatherClient weatherClient;


    @Transactional  // <- 추가
    public TodoSaveResponse saveTodo(AuthUser authUser, TodoSaveRequest todoSaveRequest) {
        User user = User.fromAuthUser(authUser);

        String weather = weatherClient.getTodayWeather();

        Todo newTodo = new Todo(
                todoSaveRequest.getTitle(),
                todoSaveRequest.getContents(),
                weather,
                user
        );
        Todo savedTodo = todoRepository.save(newTodo);

        return new TodoSaveResponse(
                savedTodo.getId(),
                savedTodo.getTitle(),
                savedTodo.getContents(),
                weather,
                new UserResponse(user.getId(), user.getEmail())
        );
    }

    // 파라미터랑 조건문 추가해주기(일단 내가 많이했던거 써보기~)
    public Page<TodoResponse> getTodos(String weather, LocalDateTime start, LocalDateTime end, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        // todos 변수 설정하는데 빈 페이지로 초기화 해주기 -> 로직을 다 태우지 못하면 초기화 할 수 있도록..
        Page<TodoResponse> todos = Page.empty();

        if(weather != null && start !=null) {
            todos = todoRepository.findAllByWeatherAndDate(weather, start, end, pageable);
        } else if (weather != null) {
            todos = todoRepository.findAllByWeather(weather, pageable);
        } else if (start != null && end != null) {
            todos = todoRepository.findAllByModifiedAtBetween(start, end, pageable);
        } else {
            todos = todoRepository.findAllByOrderByModifiedAtDesc(pageable);  // 조건없이 검색했을 때 -> 기존 코드를 if문 최종으로 넘기기
        }


        return todos.map(todo -> new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(todo.getUser().getId(), todo.getUser().getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        ));
    }

    public TodoResponse getTodo(long todoId) {
        Todo todo = todoRepository.findByIdWithUser(todoId)
                .orElseThrow(() -> new InvalidRequestException("Todo not found"));

        User user = todo.getUser();

        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(user.getId(), user.getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        );
    }
}
