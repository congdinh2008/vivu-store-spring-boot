package com.congdinh.vivustore.controllers;

import java.util.UUID;
import java.io.IOException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.congdinh.vivustore.dtos.category.CategoryDTO;
import com.congdinh.vivustore.dtos.product.ProductDTO;
import com.congdinh.vivustore.services.CategoryService;
import com.congdinh.vivustore.services.ProductService;

import jakarta.validation.Valid;
import java.nio.file.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final CategoryService categoryService;
    private final ProductService productService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String index(@RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size, Model model) {
        Sort.Direction direction = Sort.Direction.fromString(order);
        var pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        var products = productService.search(keyword, category, pageable);
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("sort", sort == null ? "name" : sort);
        model.addAttribute("order", order == null ? "asc" : order);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("totalElements", products.getTotalElements());
        model.addAttribute("pageSizes", new int[] { 2, 5, 10, 20 });

        var categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory", category);
        return "products/index";
    }

    @GetMapping("create")
    public String create(Model model) {
        var productDTO = new ProductDTO();
        model.addAttribute("productDTO", productDTO);
        var categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        return "products/create";
    }

    @PostMapping("create")
    public String createPost(@ModelAttribute @Valid ProductDTO productDTO,
            @RequestParam("thumbnailFile") MultipartFile thumbnailFile,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var categories = categoryService.getAll();
            model.addAttribute("categories", categories);
            return "products/create";
        }

        if (!thumbnailFile.isEmpty()) {
            try {
                byte[] bytes = thumbnailFile.getBytes();
                // Save thumbnail to uploads folder in static directory
                Path path = Paths.get("src/main/resources/static/uploads/" + thumbnailFile.getOriginalFilename());
                Files.write(path, bytes);
                productDTO.setThumbnail(thumbnailFile.getOriginalFilename());
            } catch (IOException e) {
                model.addAttribute("error", "Failed to upload thumbnail");
                var categories = categoryService.getAll();
                model.addAttribute("categories", categories);
                return "products/create";
            }
        }

        var product = productService.save(productDTO);

        if (product == null) {
            model.addAttribute("error", "Create product failed");
            var categories = categoryService.getAll();
            model.addAttribute("categories", categories);
            return "products/create";
        }

        return "redirect:/products";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable UUID id, RedirectAttributes redirectAttributes, Model model) {
        var product = productService.getById(id);

        if (product == null) {
            redirectAttributes.addFlashAttribute("message", "Product not found");
            return "redirect:/products";
        }

        model.addAttribute("productDTO", product);

        var categories = categoryService.getAll();
        model.addAttribute("categories", categories);

        return "products/edit";
    }

    @PostMapping("edit/{id}")
    public String editPost(@PathVariable UUID id, @ModelAttribute ProductDTO productDTO,
            @RequestParam("thumbnailFile") MultipartFile thumbnailFile,
            RedirectAttributes redirectAttributes, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var categories = categoryService.getAll();
            model.addAttribute("categories", categories);
            return "products/edit";
        }

        productDTO.setId(id);

        if (!thumbnailFile.isEmpty()) {
            try {
                byte[] bytes = thumbnailFile.getBytes();
                // Save thumbnail to uploads folder in static directory
                Path path = Paths.get("src/main/resources/static/uploads/" + thumbnailFile.getOriginalFilename());
                Files.write(path, bytes);
                productDTO.setThumbnail(thumbnailFile.getOriginalFilename());
            } catch (IOException e) {
                model.addAttribute("error", "Failed to upload thumbnail");
                var categories = categoryService.getAll();
                model.addAttribute("categories", categories);
                return "products/create";
            }
        } else {
            var product = productService.getById(id);
            productDTO.setThumbnail(product.getThumbnail());
        }

        var product = productService.save(productDTO);

        if (product == null) {
            model.addAttribute("error", "Update product failed");
            var categories = categoryService.getAll();
            model.addAttribute("categories", categories);
            return "products/edit";
        }

        redirectAttributes.addAttribute("message", "Update product successfully");
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        productService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Product deleted successfully");
        return "redirect:/products";
    }
}
