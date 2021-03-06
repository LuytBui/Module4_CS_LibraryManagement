package com.codegym.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoForm {

    private String username;

    @NotBlank
    private String email;

    private String phone;

    private String address;

    private String occupation;

    private boolean active;

    private Role role;

    private MultipartFile image;



}
