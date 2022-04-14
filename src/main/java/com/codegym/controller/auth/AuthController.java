package com.codegym.controller.auth;

import com.codegym.model.auth.ChangePasswordForm;
import com.codegym.model.auth.ErrorMessage;
import com.codegym.model.auth.JwtResponse;
import com.codegym.model.auth.UserRegisterForm;
import com.codegym.model.user.Role;
import com.codegym.model.user.User;
import com.codegym.service.auth.JwtService;
import com.codegym.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class AuthController {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    IUserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterForm userRegisterForm) {
        if (!userRegisterForm.confirmPasswordMatch()) {
            ErrorMessage errorMessage = new ErrorMessage("Mật khẩu nhập lại không khớp!");
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        Optional<User> findUser = userService.findByUsername(userRegisterForm.getUsername());
        if (findUser.isPresent()) {
            ErrorMessage errorMessage = new ErrorMessage("Tài khoản đã tồn tại!");
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(userRegisterForm.getUsername());
        String encodedPassword = passwordEncoder.encode(userRegisterForm.getPassword());
        user.setPassword(encodedPassword);
        user.setActive(true);
        Role role = new Role(3L, Role.ROLE_CUSTOMER);
        user.setRole(role);

        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        String inputUsername = user.getUsername();
        String inputPassword = user.getPassword();
        Optional<User> findUser = userService.findByUsername(inputUsername);

        if (!findUser.isPresent()) {
            ErrorMessage errorMessage = new ErrorMessage("Tài khoản không tồn tại");
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        boolean matchPassword = passwordEncoder.matches(inputPassword, findUser.get().getPassword());
        if (!matchPassword) {
            ErrorMessage errorMessage = new ErrorMessage("Mật khẩu không đúng");
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(inputUsername, inputPassword)); // tạo đối tượng Authentication
        SecurityContextHolder.getContext().setAuthentication(authentication);  // lưu đối tượng Authentication vào ContextHolder
        String jwt = jwtService.generateTokenLogin(authentication);  // tạo token từ đối tượng Authentication
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = findUser.get();

        JwtResponse jwtResponse = new JwtResponse(currentUser.getId(), jwt, userDetails.getUsername(), userDetails.getAuthorities());
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changeCurrentUserPassword(
            @RequestBody ChangePasswordForm changePasswordForm
    ) {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        if (!changePasswordForm.confirmPasswordMatch()){
            ErrorMessage errorMessage = new ErrorMessage("Mật khẩu nhập lại không khớp!");
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        String username = principal.getName();
        User user = userService.findByUsername(username).get();
        String storedPassword = user.getPassword();
        String oldPassword = changePasswordForm.getPassword();
        String newPassword = changePasswordForm.getNewPassword();

        boolean matchPassword = passwordEncoder.matches(oldPassword, storedPassword);
        if (!matchPassword){
            ErrorMessage errorMessage = new ErrorMessage("Mật khẩu không đúng!");
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        String newPasswordEncoded = passwordEncoder.encode(newPassword);
        user.setPassword(newPasswordEncoded);
        return new ResponseEntity<>(userService.save(user), HttpStatus.OK);
    }
}
