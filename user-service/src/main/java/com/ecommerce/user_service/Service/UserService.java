package com.ecommerce.user_service.Service;

import com.ecommerce.user_service.DTO.LoginRequest;
import com.ecommerce.user_service.DTO.UserRequest;
import com.ecommerce.user_service.DTO.UserResponse;
import com.ecommerce.user_service.Entity.User;
import com.ecommerce.user_service.Repository.UserRepository;
import com.ecommerce.user_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserResponse registerUser(UserRequest userRequest){
        log.info("Creating new user ....");

        //1. Map DTO -> Entity
        User user = User.builder()
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
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


    public String login(LoginRequest loginRequest){
        log.info("Attempting to login user: {}", loginRequest.getUsername());

        // 1. Find the user in the database
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 2. Verify the password
        // .matches(raw_password_from_postman, hashed_password_from_database)
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid Password");
        }

        // 3. Success!
        return jwtUtil.generateToken(user.getUsername());
    }
}
