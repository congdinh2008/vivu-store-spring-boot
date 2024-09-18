package com.congdinh.vivustore.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.congdinh.vivustore.dtos.category.CategoryDTO;

public interface CategoryService {
    List<CategoryDTO> getAll();

    Page<CategoryDTO> search(String name, Pageable pageable);

    CategoryDTO getById(UUID id);

    CategoryDTO save(CategoryDTO categoryDTO);

    void delete(UUID id);
}
