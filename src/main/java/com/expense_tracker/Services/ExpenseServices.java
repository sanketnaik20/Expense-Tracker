package com.expense_tracker.Services;

import com.expense_tracker.DTO.ExpenseRequest;
import com.expense_tracker.Entity.Category;
import com.expense_tracker.Entity.Expense;
import com.expense_tracker.Entity.UserInfo;
import com.expense_tracker.Repositories.CategoryRepository;
import com.expense_tracker.Repositories.ExpenseRepository;
import com.expense_tracker.Repositories.UserInfoRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseServices {

    @Autowired
    private ExpenseRepository expenseRepo;

    @Autowired
    private UserInfoRepository userInfoRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    public void addExpense(Expense expense, UserInfo userInfo, Category category) {
        // Fetch the UserInfo and Category from the database based on provided IDs
        UserInfo fetchedUserInfo = userInfoRepo.findById(userInfo.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category fetchedCategory = categoryRepo.findById(category.getCategory_id())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Set the fetched UserInfo and Category to the expense
        expense.setUser(fetchedUserInfo);
        expense.setCategory(fetchedCategory);

        // Save the expense
        expenseRepo.save(expense);
    }

    public List<Expense> getExpenses() {
        return expenseRepo.findAll();
    }

    public Expense updateExpense(Long expenseId, Expense updatedExpense, UserInfo userInfo, Category category) {
        // Find the existing expense
        Expense existingExpense = expenseRepo.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + expenseId));

        // Fetch the UserInfo and Category from the database based on provided IDs
        UserInfo fetchedUserInfo = userInfoRepo.findById(userInfo.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category fetchedCategory = categoryRepo.findById(category.getCategory_id())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Update the expense fields
        existingExpense.setExp_name(updatedExpense.getExp_name());
        existingExpense.setExp_amt(updatedExpense.getExp_amt());
        existingExpense.setExp_created(updatedExpense.getExp_created());
        existingExpense.setExp_note(updatedExpense.getExp_note());
        existingExpense.setExp_tag(updatedExpense.getExp_tag());
        
        // Update the relationships
        existingExpense.setUser(fetchedUserInfo);
        existingExpense.setCategory(fetchedCategory);

        // Save and return the updated expense
        return expenseRepo.save(existingExpense);
    }

}
