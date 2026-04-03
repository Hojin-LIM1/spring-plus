package org.example.expert.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.config.UserDetailsImpl;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PutMapping("/users")
    public void changePassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserChangePasswordRequest userChangePasswordRequest) {
        AuthUser authUser = new AuthUser((userDetails.getUserId()), userDetails.getEmail(), userDetails.getUserRole(), userDetails.getNickname());
        userService.changePassword(authUser.getId(), userChangePasswordRequest);
    }
}
