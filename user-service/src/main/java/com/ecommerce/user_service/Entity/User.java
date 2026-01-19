package com.ecommerce.user_service.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
}
