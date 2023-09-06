package com.controller;

import com.domain.user.data.UserData;
import com.domain.user.response.UserResponse;
import com.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/")
@Tag(name = "Auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "login/user")
    public ResponseEntity<UserResponse> loginUser(){
        UserResponse response = userService.loginUser();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "login/admin")
    public ResponseEntity<UserResponse> loginAdmin(){
        return ResponseEntity.status(HttpStatus.OK).body(new UserResponse.Builder().withData(new UserData()).build());
    }
}
