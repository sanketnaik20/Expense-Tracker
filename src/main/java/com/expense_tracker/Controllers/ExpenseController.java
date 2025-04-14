package com.expense_tracker.Controllers;

import com.expense_tracker.DTO.ExpenseRequest;
import com.expense_tracker.Entity.Category;
import com.expense_tracker.Entity.Expense;
import com.expense_tracker.Entity.UserInfo;
import com.expense_tracker.Repositories.CategoryRepository;
import com.expense_tracker.Repositories.UserInfoRepository;
import com.expense_tracker.Services.ExpenseServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ExpenseController {

    @Autowired
    private ExpenseServices expenseServices;
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    //Add Expense
    @PostMapping("/add")
    public ResponseEntity<Expense> addExpense(@RequestBody ExpenseRequest request) {
        // Create new Expense object
        Expense expense = new Expense();
        expense.setExp_name(request.getExp_name());
        expense.setExp_amt(request.getExp_amt());
        expense.setExp_created(request.getExp_created());
        expense.setExp_note(request.getExp_note());
        expense.setExp_tag(request.getExp_tag());

        // Get UserInfo and Category from repositories
        UserInfo userInfo = userInfoRepository.findById(request.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Category category = categoryRepository.findById(request.getCategory_id())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Add the expense
        expenseServices.addExpense(expense, userInfo, category);
        return new ResponseEntity<>(expense, HttpStatus.CREATED);
    }

    //Read Expense
    @GetMapping("/expenses")
    public List<Expense> getExpenses() {
        return expenseServices.getExpenses();
    }
//
   @PutMapping("/update")
   public ResponseEntity<Expense> updateExpense(@RequestParam Long id, @RequestBody ExpenseRequest request) {
       // Create updated Expense object
       Expense expense = new Expense();
       expense.setExp_name(request.getExp_name());
       expense.setExp_amt(request.getExp_amt());
       expense.setExp_created(request.getExp_created());
       expense.setExp_note(request.getExp_note());
       expense.setExp_tag(request.getExp_tag());

       // Get UserInfo and Category from repositories
       UserInfo userInfo = userInfoRepository.findById(request.getUser_id())
               .orElseThrow(() -> new RuntimeException("User not found"));
       Category category = categoryRepository.findById(request.getCategory_id())
               .orElseThrow(() -> new RuntimeException("Category not found"));

       // Update the expense
       Expense updatedExpense = expenseServices.updateExpense(id, expense, userInfo, category);
       return new ResponseEntity<>(updatedExpense, HttpStatus.OK);
   }



}
