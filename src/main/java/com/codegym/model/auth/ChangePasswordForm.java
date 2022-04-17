package com.codegym.model.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordForm {
    private String password;
    private String newPassword;
    private String confirmNewPassword;

    public boolean confirmPasswordMatch(){
        return newPassword.equals(confirmNewPassword);
    }
}
