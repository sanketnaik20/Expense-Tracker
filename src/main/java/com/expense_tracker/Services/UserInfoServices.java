package com.expense_tracker.Services;

import com.expense_tracker.Entity.UserInfo;
import com.expense_tracker.Repositories.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserInfoServices {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<UserInfo> addUser(UserInfo userInfo) {
        if (userInfoRepository.existsByName(userInfo.getName())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        UserInfo savedUser = userInfoRepository.save(userInfo);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    public ResponseEntity<List<UserInfo>> getAllUsers() {
        List<UserInfo> users = userInfoRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    public ResponseEntity<UserInfo> getUserById(Long id) {
        Optional<UserInfo> user = userInfoRepository.findById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<UserInfo> updateUser(Long id, UserInfo userInfo) {
        Optional<UserInfo> existingUser = userInfoRepository.findById(id);
        if (existingUser.isPresent()) {
            UserInfo user = existingUser.get();
            user.setName(userInfo.getName());
            user.setEmail(userInfo.getEmail());
            if (userInfo.getPassword() != null && !userInfo.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userInfo.getPassword()));
            }
            UserInfo updatedUser = userInfoRepository.save(user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Void> deleteUser(Long id) {
        if (userInfoRepository.existsById(id)) {
            userInfoRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}