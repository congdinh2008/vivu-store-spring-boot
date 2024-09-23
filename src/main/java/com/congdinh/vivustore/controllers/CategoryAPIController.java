package com.congdinh.vivustore.controllers;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.congdinh.vivustore.dtos.category.CategoryDTO;
import com.congdinh.vivustore.services.CategoryService;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping("/api/categories")
@Tag(name = "Category API")
public class CategoryAPIController {
    private final CategoryService categoryService;

    public CategoryAPIController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // API Method to handle the creation of categories via POST request
    @PostMapping
    @Operation(summary = "Create a new category")
    public ResponseEntity<?> create(@RequestBody @Valid CategoryDTO categoryDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        CategoryDTO category = categoryService.save(categoryDTO);
        if (category == null) {
            return ResponseEntity.status(500).body("Create category failed");
        }
        return ResponseEntity.ok(category);
    }

    // API Method to handle the retrieval of categories via GET request
    @GetMapping
    @Operation(summary = "Search categories")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size) {
        Sort.Direction direction = Sort.Direction.fromString(order);
        var pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        var categories = categoryService.search(keyword, pageable);
        return ResponseEntity.ok(categories);
    }

    // API Method to handle the retrieval of a single category via GET request
    @GetMapping("/{id}")
    @Operation(summary = "Get a category by id")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        var category = categoryService.getById(id);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category);
    }

    // API Method to handle the update of a category via PUT request
    @PutMapping("/{id}")
    @Operation(summary = "Update a category by id")
    public ResponseEntity<?> edit(@PathVariable UUID id, @RequestBody @Valid CategoryDTO categoryDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        categoryDTO.setId(id);
        var category = categoryService.save(categoryDTO);
        if (category == null) {
            return ResponseEntity.status(500).body("Update category failed");
        }
        return ResponseEntity.ok(category);
    }

    // API Method to handle the deletion of a category via DELETE request
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category by id")
    public ResponseEntity<?> deleteCategoryApi(@PathVariable UUID id) {
        var category = categoryService.getById(id);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        categoryService.delete(id);
        return ResponseEntity.ok().build();
    }
}