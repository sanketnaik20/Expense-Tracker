package com.expense_tracker.DTO;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ExpenseRequest {
    private Long exp_id;
    private Long user_id;
    private Long category_id;
    private String exp_name;
    private double exp_amt;
    private LocalDate exp_created;
}
