package com.expense_tracker.Entity;
import com.expense_tracker.Entity.Category;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
@Entity
@Table(name = "expense")
@Data
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exp_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo user;

    @ManyToOne
    @JoinColumn(name = "exp_category", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String exp_name;

    @Column(nullable = false)
    private double exp_amt;

    @Column(nullable = false)
    private LocalDate exp_created;

    private String exp_note;
    private String exp_tag;

    // Getters and Setters
}
