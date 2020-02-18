package com.vulenhtho.mrssso.controller.web;


import com.vulenhtho.mrssso.dto.response.UserInfoWebResponseDTO;
import com.vulenhtho.mrssso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/web")
public class UserWebController {

    private UserService userService;

    @Autowired
    public UserWebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/user-login")
    public ResponseEntity<UserInfoWebResponseDTO> getUserInfo() {
        return ResponseEntity.ok(userService.getUserLoginInfo());
    }
}
