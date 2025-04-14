package com.expense_tracker.Controllers;

import com.expense_tracker.Entity.UserInfo;
import com.expense_tracker.Repositories.UserInfoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserInfoController {

    private final UserInfoRepository userInfoRepository;

    public UserInfoController(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    // Endpoint to add a user
    @PostMapping("/add")
    public ResponseEntity<UserInfo> addUser(@RequestBody UserInfo userInfo) {
        UserInfo savedUser = userInfoRepository.save(userInfo);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // Endpoint to get all users
    @GetMapping("/")
    public ResponseEntity<List<UserInfo>> getAllUsers() {
        List<UserInfo> users = userInfoRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
