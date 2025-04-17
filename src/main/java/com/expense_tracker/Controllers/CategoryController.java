package com.expense_tracker.Controllers;

import com.expense_tracker.Entity.Category;
import com.expense_tracker.Repositories.CategoryRepository;
import com.expense_tracker.Services.CategoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    CategoryServices categoryServices;
    // POST method to create a new category
    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return categoryServices.createCategory(category);
    }

    // Get all categories
    @GetMapping("/")
    public ResponseEntity<List<Category>> getAllCategories() {
        return categoryServices.getAllCategories();
    }
     //delete category
     @DeleteMapping("/{id}")
     public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        return categoryServices.deleteCategory(id);
     }

     //update category
     @PutMapping("/{id}")
     public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return categoryServices.updateCategory(id, category);
     }
}
