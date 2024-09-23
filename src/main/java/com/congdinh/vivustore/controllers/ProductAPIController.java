package com.congdinh.vivustore.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.congdinh.vivustore.dtos.category.CategoryDTO;
import com.congdinh.vivustore.dtos.product.ProductCreateDTO;
import com.congdinh.vivustore.dtos.product.ProductDTO;
import com.congdinh.vivustore.dtos.product.ProductUpdateDTO;
import com.congdinh.vivustore.services.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/api/products")
@Tag(name = "Product API")
public class ProductAPIController {
    private final ProductService productService;

    public ProductAPIController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Search products")
    public ResponseEntity<Page<ProductDTO>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size) {
        Sort.Direction direction = Sort.Direction.fromString(order);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        var products = productService.search(keyword, category, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product by id")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        var product = productService.getById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @PostMapping
    @Operation(summary = "Create a new product")
    public ResponseEntity<?> create(@RequestBody @Valid ProductCreateDTO productCreateDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        ProductDTO product = productService.save(productCreateDTO);
        if (product == null) {
            return ResponseEntity.status(500).body("Create product failed");
        }
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product by id")
    public ResponseEntity<?> edit(@PathVariable UUID id, @RequestBody @Valid ProductUpdateDTO productUpdateDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        productUpdateDTO.setId(id);
        var product = productService.save(productUpdateDTO);
        if (product == null) {
            return ResponseEntity.status(500).body("Update product failed");
        }
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product by id")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        var product = productService.getById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        productService.delete(id);
        return ResponseEntity.ok().build();
    }
}
