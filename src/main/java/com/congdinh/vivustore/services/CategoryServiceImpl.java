package com.congdinh.vivustore.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.congdinh.vivustore.dtos.category.CategoryDTO;
import com.congdinh.vivustore.entities.Category;
import com.congdinh.vivustore.repositories.CategoryRepository;

// Register Bean
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDTO> getAll() {
        // Get all categories - category model
        var categories = categoryRepository.findAll();

        // Transform to DTO
        return categories.stream().map(category -> {
            var categoryDTO = new CategoryDTO();
            categoryDTO.setId(category.getId());
            categoryDTO.setName(category.getName());
            categoryDTO.setDescription(category.getDescription());
            return categoryDTO;
        }).toList();
    }

    @Override
    public CategoryDTO getById(UUID id) {
        var category = categoryRepository.findById(id).orElse(null);

        if (category == null) {
            throw new IllegalArgumentException("Category not found");
        }

        var categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());

        return categoryDTO;
    }

    @Override
    public CategoryDTO save(CategoryDTO categoryDTO) {
        var category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        var savedCategory = categoryRepository.save(category);

        if (savedCategory == null) {
            throw new RuntimeException("Save category failed");
        }

        var savedCategoryDTO = new CategoryDTO();
        savedCategoryDTO.setId(savedCategory.getId());
        savedCategoryDTO.setName(savedCategory.getName());
        savedCategoryDTO.setDescription(savedCategory.getDescription());

        return savedCategoryDTO;
    }

    @Override
    public void delete(UUID id) {
        var existingCategory = categoryRepository.findById(id).orElse(null);

        if (existingCategory == null) {
            throw new IllegalArgumentException("Category not found");
        }

        categoryRepository.delete(existingCategory);
    }
}
