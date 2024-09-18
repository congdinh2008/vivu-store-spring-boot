package com.congdinh.vivustore.services;

import java.util.List;
import java.util.UUID;

import com.congdinh.vivustore.dtos.category.CategoryDTO;

public interface CategoryService {
    List<CategoryDTO> getAll();

    CategoryDTO getById(UUID id);

    CategoryDTO save(CategoryDTO categoryDTO);

    void delete(UUID id);
}
