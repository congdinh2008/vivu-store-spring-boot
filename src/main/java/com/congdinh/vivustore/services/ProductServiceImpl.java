package com.congdinh.vivustore.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.congdinh.vivustore.dtos.category.CategoryDTO;
import com.congdinh.vivustore.dtos.product.ProductDTO;
import com.congdinh.vivustore.entities.Product;
import com.congdinh.vivustore.repositories.CategoryRepository;
import com.congdinh.vivustore.repositories.ProductRepository;

import jakarta.persistence.criteria.Predicate;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<ProductDTO> getAll() {
        // Get all products - product model
        var products = productRepository.findAll();

        // Transform to DTO
        return products.stream().map(product -> {
            var productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setDescription(product.getDescription());
            productDTO.setThumbnail(product.getThumbnail());
            productDTO.setPrice(product.getPrice());
            productDTO.setStock(product.getStock());
            productDTO.setCategoryId(product.getCategory().getId());

            // Set category
            var categoryDTO = new CategoryDTO();
            categoryDTO.setId(product.getCategory().getId());
            categoryDTO.setName(product.getCategory().getName());
            productDTO.setCategory(categoryDTO);
            return productDTO;
        }).toList();
    }

    @Override
    public ProductDTO getById(UUID id) {
        var product = productRepository.findById(id).orElse(null);

        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }

        var productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setThumbnail(product.getThumbnail());
        productDTO.setPrice(product.getPrice());
        productDTO.setStock(product.getStock());
        productDTO.setCategoryId(product.getCategory().getId());

        // Set category
        var categoryDTO = new CategoryDTO();
        categoryDTO.setId(product.getCategory().getId());
        categoryDTO.setName(product.getCategory().getName());
        productDTO.setCategory(categoryDTO);

        return productDTO;
    }

    @Override
    public ProductDTO save(ProductDTO productDTO) {
        if(productDTO == null){
            throw new IllegalArgumentException("Product is required");
        }

        var product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setThumbnail(productDTO.getThumbnail());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());

        if(productDTO.getCategoryId() != null){
            var category = categoryRepository.findById(productDTO.getCategoryId()).orElse(null);
            if(category == null){
                throw new IllegalArgumentException("Category not found");
            }
            product.setCategory(category);
        }

        var savedProduct = productRepository.save(product);

        if (savedProduct == null) {
            throw new RuntimeException("Save product failed");
        }

        var savedProductDTO = new ProductDTO();
        savedProductDTO.setId(savedProduct.getId());
        savedProductDTO.setName(savedProduct.getName());
        savedProductDTO.setDescription(savedProduct.getDescription());
        savedProductDTO.setThumbnail(savedProduct.getThumbnail());
        savedProductDTO.setPrice(savedProduct.getPrice());
        savedProductDTO.setStock(savedProduct.getStock());
        savedProductDTO.setCategoryId(savedProduct.getCategory().getId());

        return savedProductDTO;
    }

    @Override
    public void delete(UUID id) {
        var existingProduct = productRepository.findById(id).orElse(null);

        if (existingProduct == null) {
            throw new IllegalArgumentException("Product not found");
        }

        productRepository.delete(existingProduct);
    }

    @Override
    public Page<ProductDTO> search(String name, String categoryName, Pageable pageable) {
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            if (name == null) {
                return null;
            }

            Predicate predicate = criteriaBuilder.or(
                    criteriaBuilder.like(root.get("name"), "%" + name + "%"),
                    criteriaBuilder.like(root.get("description"), "%" + name + "%"));

            if (categoryName != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("category").get("name"), categoryName));
            }

            return predicate;
        };

        var products = productRepository.findAll(spec, pageable);

        return products.map(product -> {
            var productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setDescription(product.getDescription());
            productDTO.setThumbnail(product.getThumbnail());
            productDTO.setPrice(product.getPrice());
            productDTO.setStock(product.getStock());
            productDTO.setCategoryId(product.getCategory().getId());

            // Set category
            var categoryDTO = new CategoryDTO();
            categoryDTO.setId(product.getCategory().getId());
            categoryDTO.setName(product.getCategory().getName());
            productDTO.setCategory(categoryDTO);
            return productDTO;
        });
    }
}
