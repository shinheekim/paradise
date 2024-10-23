package com.example.paradise.domain.user.api;

import com.example.paradise.domain.user.application.UserService;
import com.example.paradise.domain.user.domain.User;
import com.example.paradise.domain.user.dto.UserDeleteRequest;
import com.example.paradise.domain.user.dto.UserLoginRequest;
import com.example.paradise.domain.user.dto.UserPasswordUpdateRequest;
import com.example.paradise.domain.user.dto.UserRegisterRequest;

import com.example.paradise.domain.user.util.JwtTokenUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegisterRequest request) {
        userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("/login");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody @Valid UserLoginRequest request) {
        User user = userService.loginUser(request.getEmail(), request.getPassword());
        String token = jwtTokenUtil.createToken(request.getEmail(), user.getRole());
        return ResponseEntity.ok(JwtTokenUtil.BEARER_PREFIX + token);
    }
    // 모든 회원 조회
    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    // 특정 회원 조회
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
    // 비밀번호 변경
    @PutMapping("/update")
    public ResponseEntity<User> updatePassword(@RequestBody @Valid UserPasswordUpdateRequest request) {
        User updatedUser = userService.updatePassword(request.getEmail(), request.getNewPassword(), request.getConfirmPassword());
        return ResponseEntity.ok(updatedUser);
    }
    // 회원 탈퇴
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id, @RequestBody @Valid UserDeleteRequest request) {
        userService.deleteUser(id, request.getPassword());
        return ResponseEntity.ok("회원탈퇴 성공");
    }
}