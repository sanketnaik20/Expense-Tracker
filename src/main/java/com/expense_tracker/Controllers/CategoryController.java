package com.expense_tracker.Controllers;

import com.expense_tracker.Entity.Category;
import com.expense_tracker.Repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // POST method to create a new category
    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        // Save the new category in the database
        Category savedCategory = categoryRepository.save(category);

        // Return the saved category as a response
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }
}
