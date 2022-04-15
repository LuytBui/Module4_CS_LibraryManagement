package com.codegym.controller.user;

import com.codegym.model.user.User;
import com.codegym.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/manage_user")
public class UserController {
    @Autowired
    IUserService userService;

    @PostMapping("/{id}/deactive")
    public ResponseEntity<?> deactiveUser(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (!user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        user.get().setActive(false);
        return new ResponseEntity<>(userService.save(user.get()), HttpStatus.OK);
    }

    @PostMapping("/{id}/active")
    public ResponseEntity<?> activeUser(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (!user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        user.get().setActive(true);
        return new ResponseEntity<>(userService.save(user.get()), HttpStatus.OK);
    }
}
