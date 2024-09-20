package com.congdinh.vivustore.controllers;

import java.util.UUID;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.congdinh.vivustore.dtos.category.CategoryDTO;
import com.congdinh.vivustore.services.CategoryService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String index(
            @RequestParam(required = false) String keyword, // Keyword - Từ khoá dùng để tìm kiếm theo điều kiện =>
                                                            // Specification
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(defaultValue = "0") int page, // Page Index - Trang thứ bao nhiêu
            @RequestParam(defaultValue = "2") int size, // Page size - Số lượng items mỗi page
            Model model) {
        Sort.Direction direction = Sort.Direction.fromString(order); // Convert string to Direction
        var pageable = PageRequest.of(page, size, Sort.by(direction, sort)); // Create Pageable object with page index,
                                                                             // page size and sortable

        // Query data from database based on keyword and pageable
        var categories = categoryService.search(keyword, pageable);

        // Pass Page<CategoryDTO> to view
        model.addAttribute("categories", categories);

        // Pass current keyword to view to show in keyword input of search form
        model.addAttribute("keyword", keyword == null ? "" : keyword);

        // Pass current page to view
        model.addAttribute("currentPage", page);

        // Pass current sort to view
        model.addAttribute("sort", sort == null ? "name" : sort);

        // Pass current direction to view
        model.addAttribute("order", order == null ? "asc" : order);

        // Pass current page size to view
        model.addAttribute("pageSize", size);

        // Using pagination
        model.addAttribute("totalPages", categories.getTotalPages());
        model.addAttribute("totalElements", categories.getTotalElements());
        model.addAttribute("pageSizes", new int[] { 2, 5, 10, 20 });
        return "categories/index";
    }

    @GetMapping("create")
    public String create(Model model) {
        var categoryDTO = new CategoryDTO();
        model.addAttribute("categoryDTO", categoryDTO);
        return "categories/create";
    }

    @PostMapping("create")
    public String createPost(
            @ModelAttribute @Valid CategoryDTO categoryDTO,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return "categories/create";
        }

        var category = categoryService.save(categoryDTO);

        if (category == null) {
            model.addAttribute("message", "Create category failed");
            return "categories/create";
        }

        return "redirect:/categories";
    }

    // API Method to handle the creation of categories via POST request
    @PostMapping("/api/create")
    public ResponseEntity<?> createCategoryApi(@RequestBody @Valid CategoryDTO categoryDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        CategoryDTO category = categoryService.save(categoryDTO);
        if(category == null){
            return ResponseEntity.ofNullable(null);
        }
        return ResponseEntity.ok(category);
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable UUID id, RedirectAttributes redirectAttributes, Model model) {
        var category = categoryService.getById(id);

        if (category == null) {
            redirectAttributes.addFlashAttribute("message", "Category not found");
            return "redirect:/categories";
        }

        model.addAttribute("categoryDTO", category);

        return "categories/edit";
    }

    @PostMapping("edit/{id}")
    public String editPost(@PathVariable UUID id, @ModelAttribute CategoryDTO categoryDTO,
            RedirectAttributes redirectAttributes, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "categories/edit";
        }

        categoryDTO.setId(id);

        var category = categoryService.save(categoryDTO);

        if (category == null) {
            model.addAttribute("error", "Update category failed");
            return "categories/edit";
        }

        redirectAttributes.addAttribute("message", "Update category successfully");
        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        categoryService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Category deleted successfully");
        return "redirect:/categories";
    }
}
