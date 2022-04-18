package com.codegym.controller.user;

import com.codegym.model.auth.ErrorMessage;
import com.codegym.model.user.Role;
import com.codegym.model.user.User;
import com.codegym.model.user.UserInfoForm;
import com.codegym.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/users")
public class UserController {


    @Autowired
    IUserService userService;

    @Value("${file-upload}")
    private String uploadPath;

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (!user.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }



    @PostMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @ModelAttribute UserInfoForm userInfoForm) {
        Optional<User> updateUserOptional = userService.findById(id);
        if (!updateUserOptional.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        User updateUser = updateUserOptional.get();
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        User logginUser = userService.findByUsername(principal.getName()).get();
        boolean currentUserIsAdmin = logginUser.getRole().getName().equals(Role.ROLE_ADMIN);
        boolean currentUserIsOwner = logginUser.getId().equals(id);
        boolean authorized = currentUserIsAdmin || currentUserIsOwner;

        if (!authorized){
            ErrorMessage errorMessage = new ErrorMessage("Không có quyền sửa thông tin người dùng");
            return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
        }

        updateUser.setEmail(userInfoForm.getEmail());
        updateUser.setPhone(userInfoForm.getPhone());
        updateUser.setAddress(userInfoForm.getAddress());
        updateUser.setOccupation(userInfoForm.getOccupation());
        updateUser.setActive(userInfoForm.isActive());
        if (currentUserIsAdmin){  // chỉ admin mới có quyền thay đổi role
            updateUser.setRole(userInfoForm.getRole());
        }

        MultipartFile img = userInfoForm.getImage();
        if (img != null && img.getSize() != 0) {
            String fileName = img.getOriginalFilename();
            long currentTime = System.currentTimeMillis();
            fileName = currentTime + "_" + fileName;
            try {
                FileCopyUtils.copy(img.getBytes(), new File(uploadPath + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateUser.setImage(fileName);
        }

        return new ResponseEntity<>(userService.save(updateUser), HttpStatus.OK);
    }
}
