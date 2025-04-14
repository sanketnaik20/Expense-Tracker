package com.expense_tracker.Entity;

import com.expense_tracker.Entity.Expense;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "category")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long category_id;

    private String category_name;

    // One category can be used by many expenses
//    @OneToMany(mappedBy = "category")
//    @JsonManagedReference
//    private Set<Expense> expenses;

    // Getters and Setters
}
