package com.expense_tracker.Services;

import com.expense_tracker.Entity.UserInfo;
import com.expense_tracker.Repositories.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserInfoServices {

    @Autowired
    private UserInfoRepository userInfoRepository;

    public ResponseEntity<UserInfo> addUser(UserInfo userInfo) {
        // Check if user already exists
        if (userInfoRepository.existsByName(userInfo.getName())) {
            throw new RuntimeException("User with name " + userInfo.getName() + " already exists");
        }

        // Save the user
        UserInfo savedUser = userInfoRepository.save(userInfo);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    public ResponseEntity<List<UserInfo>> getAllUsers() {
        List<UserInfo> users = userInfoRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    public ResponseEntity<UserInfo> getUserById(Long id) {
        UserInfo user = userInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public ResponseEntity<UserInfo> updateUser(Long id, UserInfo updatedUserInfo) {
        // Check if user exists
        UserInfo existingUser = userInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Update user information
        existingUser.setName(updatedUserInfo.getName());

        // Save the updated user
        UserInfo savedUser = userInfoRepository.save(existingUser);
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }

    public ResponseEntity<Void> deleteUser(Long id) {
        // Check if user exists
        if (!userInfoRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }

        // Delete the user
        userInfoRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}