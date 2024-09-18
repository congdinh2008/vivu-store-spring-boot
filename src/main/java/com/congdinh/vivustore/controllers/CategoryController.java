package com.congdinh.vivustore.controllers;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String index(Model model) {
        var categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        return "categories/index";
    }

    @GetMapping("create")
    public String create(Model model) {
        var categoryDTO = new CategoryDTO();
        model.addAttribute("categoryDTO", categoryDTO);
        return "categories/create";
    }

    @PostMapping("create")
    public String createPost(@ModelAttribute @Valid CategoryDTO categoryDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "categories/create";
        }

        var category = categoryService.save(categoryDTO);

        if (category == null) {
            model.addAttribute("error", "Create category failed");
            return "categories/create";
        }

        return "redirect:/categories";
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
