package com.expense_tracker.Controllers;

import com.expense_tracker.Entity.UserInfo;
import com.expense_tracker.Services.UserInfoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserInfoController {

    private final UserInfoServices userInfoServices;

    @Autowired
    public UserInfoController(UserInfoServices userInfoServices) {
        this.userInfoServices = userInfoServices;
    }

    // Create user
    @PostMapping("/add")
    public ResponseEntity<UserInfo> addUser(@RequestBody UserInfo userInfo) {
        return userInfoServices.addUser(userInfo);
    }

    // Get all users
    @GetMapping("/")
    public ResponseEntity<List<UserInfo>> getAllUsers() {
        return userInfoServices.getAllUsers();
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserInfo> getUserById(@PathVariable("id") Long id) {
        return userInfoServices.getUserById(id);
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<UserInfo> updateUser(@PathVariable("id") Long id, @RequestBody UserInfo userInfo) {
        return userInfoServices.updateUser(id, userInfo);
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        return userInfoServices.deleteUser(id);
    }
}
