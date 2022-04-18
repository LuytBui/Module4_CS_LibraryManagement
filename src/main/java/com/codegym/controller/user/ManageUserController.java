package com.codegym.controller.user;

import com.codegym.model.user.Role;
import com.codegym.model.user.User;
import com.codegym.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/manage_user")
public class ManageUserController {
    public final int PAGE_SIZE = 12;

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

    @GetMapping("/librarians")
    public ResponseEntity<?> findAllLibrarian(@RequestParam(name="page") Long page){
        if (page == null) page = 0L;
        Pageable pageable = PageRequest.of(page.intValue(), PAGE_SIZE);
        Page<User> librarians = userService.finddAllUserByRole_Name(Role.ROLE_LIBRARIAN, pageable);

        return new ResponseEntity<>(librarians, HttpStatus.OK);
    }

    @GetMapping("/customers")
    public ResponseEntity<?> findAllCustomer(@RequestParam(name="page") Long page){
        if (page == null) page = 0L;
        Pageable pageable = PageRequest.of(page.intValue(), PAGE_SIZE);
        Page<User> librarians = userService.finddAllUserByRole_Name(Role.ROLE_CUSTOMER, pageable);

        return new ResponseEntity<>(librarians, HttpStatus.OK);
    }

}
