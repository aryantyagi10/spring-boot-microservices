package com.ecommerce.user_service.Service;

import com.ecommerce.user_service.DTO.UserRequest;
import com.ecommerce.user_service.DTO.UserResponse;
import com.ecommerce.user_service.Entity.User;
import com.ecommerce.user_service.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserResponse registerUser(UserRequest userRequest){
        log.info("Creating new user ....");

        //1. Map DTO -> Entity
        User user = User.builder()
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();

        //The above code block explained -
        //"Start building a new User. Set their username to whatever is in the request.
        // Set their email to whatever is in the request. Set their password to whatever is in the request.
        // Okay, now build the final object."

        //2. Save to DB
        userRepository.save(user);
        log.info("User {} saved successfully", user.getId());

        //3. Map Entity -> DTO
        return mapToUserResponse(user);
    }

    public UserResponse getUserById(Long id){
        return userRepository.findById(id)
                .map(this::mapToUserResponse)
                .orElseThrow(() -> new RuntimeException("User with Id : " + id + " not found"));
    }

    public List<UserResponse> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    private UserResponse mapToUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
