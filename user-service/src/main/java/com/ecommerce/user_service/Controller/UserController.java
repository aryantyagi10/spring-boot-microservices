package com.ecommerce.user_service.Controller;

import com.ecommerce.user_service.DTO.UserRequest;
import com.ecommerce.user_service.DTO.UserResponse;
import com.ecommerce.user_service.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerUser(@RequestBody @Valid UserRequest userRequest){
        return userService.registerUser(userRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }
}
