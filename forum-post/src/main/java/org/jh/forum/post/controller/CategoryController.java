package org.jh.forum.post.controller;

import org.jh.forum.post.dto.CategoryDTO;
import org.jh.forum.post.model.Category;
import org.jh.forum.post.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/post/category")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @PostMapping("/create")
    public void addCategory(@RequestBody @Validated CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        categoryService.save(category);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Integer id) {
        categoryService.removeById(id);
    }

    @PutMapping("/{id}")
    public void updateCategory(@RequestBody @Validated Category category) {
        categoryService.updateById(category);
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Integer id) {
        return categoryService.getById(id);
    }

    @GetMapping("/list")
    public List<Category> getAllCategories() {
        return categoryService.list();
    }
}
