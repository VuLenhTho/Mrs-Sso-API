package com.vulenhtho.mrssso.controller.web;


import com.vulenhtho.mrssso.dto.response.UserInfoWebResponseDTO;
import com.vulenhtho.mrssso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/web")
public class UserWebController {

    private final UserService userService;

    @Autowired
    public UserWebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/user-login")
    public ResponseEntity<UserInfoWebResponseDTO> getUserInfo() {
        return ResponseEntity.ok(userService.getUserLoginInfo());
    }

    @GetMapping("/user/check-duplicates-user-info")
    public ResponseEntity<String> checkDuplicatesUserInfoInCreate(@RequestParam String userName, @RequestParam String email, @RequestParam String phone) {
        return ResponseEntity.ok(userService.checkDuplicatesUserInfoInCreate(userName, email, phone));
    }

    @GetMapping("/user/check-duplicates-user-info-for-update")
    public ResponseEntity<String> checkDuplicatesUserInfoInUpdate(@RequestParam String userName, @RequestParam String email, @RequestParam String phone) {
        return ResponseEntity.ok(userService.checkDuplicatesUserInfoInUpdate(userName, email, phone));
    }
}
