package com.expense_tracker.Controllers;

import com.expense_tracker.DTO.ExpenseRequest;
import com.expense_tracker.DTO.ExpenseStatistics;
import com.expense_tracker.Entity.Expense;
import com.expense_tracker.Services.ExpenseServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ExpenseController {

    private final ExpenseServices expenseServices;

    @Autowired
    public ExpenseController(ExpenseServices expenseServices) {
        this.expenseServices = expenseServices;
    }

    //Add Expense
    @PostMapping("/add")
    public ResponseEntity<Expense> addExpense(@RequestBody ExpenseRequest request) {
        return expenseServices.addExpense(request);
    }

    //Read Expense
    @GetMapping("/expenses")
    public ResponseEntity<List<Expense>> getExpenses() {
        return expenseServices.getExpenses();
    }

    //update Expense
    @PutMapping("/update")
    public ResponseEntity<Expense> updateExpense(@RequestParam Long id, @RequestBody ExpenseRequest request) {
        return expenseServices.updateExpense(id, request);
    }

    //delete Expense
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteExpense(@RequestParam Long id) {
        return expenseServices.deleteExpense(id);
    }

    //Get Statistics
    @GetMapping("/statistics")
    public ResponseEntity<ExpenseStatistics> getExpenseStatistics() {
        return expenseServices.getExpenseStatistics();
    }
}
