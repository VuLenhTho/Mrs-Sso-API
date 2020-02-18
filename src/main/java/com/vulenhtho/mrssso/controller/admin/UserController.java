package com.vulenhtho.mrssso.controller.admin;

import com.vulenhtho.mrssso.dto.UserDTO;
import com.vulenhtho.mrssso.dto.request.IdsRequestDTO;
import com.vulenhtho.mrssso.dto.request.UserFilterRequestDTO;
import com.vulenhtho.mrssso.mapper.UserMapper;
import com.vulenhtho.mrssso.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserDTO>> getAllByFilter(
            @RequestParam(required = false, defaultValue = "0") Integer page
            , @RequestParam(required = false, defaultValue = "5") Integer size
            , @RequestParam(required = false) String search, @RequestParam(required = false) Boolean locked
            , @RequestParam(required = false) String sort, @RequestParam(required = false) Boolean sex
            , @RequestParam(required = false) Boolean activated, @RequestParam(required = false) String roles) {

        UserFilterRequestDTO userFilterRequestDTO = new UserFilterRequestDTO(sort, sex, activated, locked
                , search, roles, page, size);

        return ResponseEntity.ok(userService.getAllUserWithFilter(userFilterRequestDTO));
    }

    @PostMapping("/user")
    public ResponseEntity<?> create(@RequestBody UserDTO userDTO){
        if (userService.createdUser(userDTO) != null){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("Can not create user "+ userDTO.getUserName() +", check data");
    }

    @PutMapping("/user")
    public ResponseEntity<?> update(@RequestBody UserDTO userDTO){
        if (userService.update(userDTO)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found user with userName:" + userDTO.getUserName());
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        if (userService.delete(id)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("not found user with id:" +id.toString());
    }

    @DeleteMapping("/users")
    public ResponseEntity<?> delete(@RequestBody IdsRequestDTO ids){
        if (userService.delete(ids.getIds())){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("not found user with id:" + ids.getIds().toString());
    }

}
