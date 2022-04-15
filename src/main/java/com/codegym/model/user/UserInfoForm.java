package com.codegym.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoForm {
    private Long id;

    private String username;

    private String email;

    private String phone;

    private String address;

    private String occupation;

    private MultipartFile image;

}
