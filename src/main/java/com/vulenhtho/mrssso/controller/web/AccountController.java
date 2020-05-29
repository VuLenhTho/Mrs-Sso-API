package com.vulenhtho.mrssso.controller.web;

import com.vulenhtho.mrssso.dto.PasswordChangeDTO;
import com.vulenhtho.mrssso.dto.UserDTO;
import com.vulenhtho.mrssso.entity.User;
import com.vulenhtho.mrssso.repository.UserRepository;
import com.vulenhtho.mrssso.service.UserService;
import com.vulenhtho.mrssso.service.impl.MailServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final UserRepository userRepository;

    private final UserService userService;

    private final MailServiceImpl mailService;

    public AccountController(UserRepository userRepository, UserService userService, MailServiceImpl mailService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@RequestBody UserDTO userDTO) {
        User user = userService.registerUser(userDTO);
        mailService.sendActivationEmail(userDTO, user.getActivationKey());
    }

    @GetMapping("/activate")
    public ResponseEntity<Void> activateAccount(@RequestParam String key) {
        if (userService.activateRegistration(key)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(path = "/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        boolean result = userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
        if (result){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(path = "/reset-password/init")
    public ResponseEntity<Void> requestPasswordReset(@RequestBody UserDTO email) {
        boolean result = mailService.sendPasswordResetMail(userService.requestPasswordReset(email.getEmail()));
        if (result){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(path = "/reset-password/finish")
    public ResponseEntity<Void> finishPasswordReset(@RequestBody UserDTO keyAndPassword) {
        User user = userService.completePasswordReset(keyAndPassword.getPassword(), keyAndPassword.getResetKey());
        if (user != null){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
