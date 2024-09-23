package com.congdinh.vivustore.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.congdinh.vivustore.dtos.product.ProductCreateDTO;
import com.congdinh.vivustore.dtos.product.ProductDTO;
import com.congdinh.vivustore.dtos.product.ProductUpdateDTO;

public interface ProductService {
    List<ProductDTO> getAll();

    Page<ProductDTO> search(String name, String categoryName, Pageable pageable);

    ProductDTO getById(UUID id);

    ProductDTO save(ProductDTO productDTO);

    ProductDTO save(ProductCreateDTO productCreateDTO);

    ProductDTO save(ProductUpdateDTO productUpdateDTO);

    void delete(UUID id);
}
