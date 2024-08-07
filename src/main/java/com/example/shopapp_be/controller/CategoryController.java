package com.example.shopapp_be.controller;


import com.example.shopapp_be.dto.CategoryDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    // /api/v1/category?page=1&limit=10
    @GetMapping
    public ResponseEntity<String> getAllCategory(
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit
    ) {
        return ResponseEntity.ok(String.format("Category, page: " + page + ", limit: " + limit));
    }

    @PostMapping
//    Nếu tham số truyền vào là 1 object thì sao ? => data transfer object (DTO) => request object
    public ResponseEntity<?> addCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
          List<String> errors = bindingResult
                  .getFieldErrors()
                  .stream()
                  .map(FieldError::getDefaultMessage)
                  .toList();
          return ResponseEntity.badRequest().body(errors.toString());
        }
        return ResponseEntity.ok("Category added " + categoryDTO.getName());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id) {
        return ResponseEntity.ok("Category updated " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        return ResponseEntity.ok("Category deleted " + id);
    }
}
