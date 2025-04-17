package com.expense_tracker.Services;

import com.expense_tracker.DTO.ExpenseRequest;
import com.expense_tracker.DTO.ExpenseStatistics;
import com.expense_tracker.Entity.Category;
import com.expense_tracker.Entity.Expense;
import com.expense_tracker.Entity.UserInfo;
import com.expense_tracker.Repositories.CategoryRepository;
import com.expense_tracker.Repositories.ExpenseRepository;
import com.expense_tracker.Repositories.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseServices {

    @Autowired
    private ExpenseRepository expenseRepo;

    @Autowired
    private UserInfoRepository userInfoRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    public ResponseEntity<Expense> addExpense(ExpenseRequest request) {
        // Create new Expense object
        Expense expense = new Expense();
        expense.setExp_name(request.getExp_name());
        expense.setExp_amt(request.getExp_amt());
        expense.setExp_created(request.getExp_created());

        // Get UserInfo and Category from repositories
        UserInfo userInfo = userInfoRepo.findById(request.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Category category = categoryRepo.findById(request.getCategory_id())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Set the relationships
        expense.setUser(userInfo);
        expense.setCategory(category);

        // Save and return the expense
        Expense savedExpense = expenseRepo.save(expense);
        return new ResponseEntity<>(savedExpense, HttpStatus.CREATED);
    }

    public ResponseEntity<List<Expense>> getExpenses() {
        return new ResponseEntity<>(expenseRepo.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<Expense> updateExpense(Long id, ExpenseRequest request) {
        // Find the existing expense
        Expense existingExpense = expenseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));

        // Get UserInfo and Category from repositories
        UserInfo userInfo = userInfoRepo.findById(request.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Category category = categoryRepo.findById(request.getCategory_id())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Update the expense fields
        existingExpense.setExp_name(request.getExp_name());
        existingExpense.setExp_amt(request.getExp_amt());
        existingExpense.setExp_created(request.getExp_created());
        
        // Update the relationships
        existingExpense.setUser(userInfo);
        existingExpense.setCategory(category);

        // Save and return the updated expense
        return new ResponseEntity<>(expenseRepo.save(existingExpense), HttpStatus.OK);
    }

    public ResponseEntity<Void> deleteExpense(Long id) {
        // Check if expense exists
        if (!expenseRepo.existsById(id)) {
            throw new RuntimeException("Expense not found with id: " + id);
        }

        // Delete the expense
        expenseRepo.deleteById(id);
        
        // Return success response
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<ExpenseStatistics> getExpenseStatistics() {
        List<Expense> allExpenses = expenseRepo.findAll();
        
        if (allExpenses.isEmpty()) {
            throw new RuntimeException("No expenses found");
        }

        ExpenseStatistics statistics = new ExpenseStatistics();

        // Calculate total expenses
        double total = allExpenses.stream()
                .mapToDouble(Expense::getExp_amt)
                .sum();
        statistics.setTotalExpenses(total);

        // Calculate average expense
        double average = total / allExpenses.size();
        statistics.setAverageExpense(average);

        // Calculate expenses by category
        Map<String, Double> expensesByCategory = allExpenses.stream()
                .collect(Collectors.groupingBy(
                    expense -> expense.getCategory().getCategory_name(),
                    Collectors.summingDouble(Expense::getExp_amt)
                ));
        statistics.setExpensesByCategory(expensesByCategory);

        // Find highest and lowest expenses
        double highest = allExpenses.stream()
                .mapToDouble(Expense::getExp_amt)
                .max()
                .orElse(0.0);
        statistics.setHighestExpense(highest);

        double lowest = allExpenses.stream()
                .mapToDouble(Expense::getExp_amt)
                .min()
                .orElse(0.0);
        statistics.setLowestExpense(lowest);

        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }
}
