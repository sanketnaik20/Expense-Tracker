package com.expense_tracker.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "user_info")
@Data
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(nullable = false)
    private String name;

    // One user can have many expenses
//    @JsonManagedReference
//    @OneToMany(mappedBy = "user")
//    private List<Expense> expenses;



    // Getters and Setters
}
