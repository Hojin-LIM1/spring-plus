package org.example.expert.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.config.UserDetailsImpl;
import org.example.expert.domain.comment.dto.request.CommentSaveRequest;
import org.example.expert.domain.comment.dto.response.CommentResponse;
import org.example.expert.domain.comment.dto.response.CommentSaveResponse;
import org.example.expert.domain.comment.service.CommentService;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/todos/{todoId}/comments")
    public ResponseEntity<CommentSaveResponse> saveComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable long todoId,
            @Valid @RequestBody CommentSaveRequest commentSaveRequest

    ) {// 이거 authUser가 너무 많아서 메서드 인자 타입을 UserDetailsImpl로 바꿔야 겟당
        // 서비스 로직 잘못건드렸다간 망할거 같음요..
        AuthUser authUser = new AuthUser((userDetails.getUserId()), userDetails.getEmail(), userDetails.getUserRole(), userDetails.getNickname());
        return ResponseEntity.ok(commentService.saveComment(authUser, todoId, commentSaveRequest));
    }

    // 로그 분석 : 댓글조회시 마다 사용자 정보를 달라고함 ㄷㄷ
    // entity와 레포 점검 ㄱㄱ~~

    @GetMapping("/todos/{todoId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable long todoId) {
        return ResponseEntity.ok(commentService.getComments(todoId));
    }
}
