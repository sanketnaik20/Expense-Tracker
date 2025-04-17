package com.expense_tracker.Repositories;

import com.expense_tracker.Entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    boolean existsByName(String name);
    Optional<UserInfo> findByName(String name);
}
