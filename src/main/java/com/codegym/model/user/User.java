package com.codegym.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String username;
    private String password;

    private String email;
    private String phone;

    private String image;

    private String address;     // optional
    private String occupation;  // optional

    private boolean active;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
