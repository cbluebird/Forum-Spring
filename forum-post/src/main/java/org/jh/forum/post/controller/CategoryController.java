package org.jh.forum.post.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.NotNull;
import org.jh.forum.common.api.Pagination;
import org.jh.forum.post.dto.CategoryDTO;
import org.jh.forum.post.model.Category;
import org.jh.forum.post.service.ICategoryService;
import org.jh.forum.post.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/post/category")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IUserService userService;

    @PostMapping
    public void addCategory(@RequestHeader("X-User-ID") String userId, @RequestBody @Validated CategoryDTO categoryDTO) {
        userService.checkAdminPermission(userId);
        categoryService.addCategory(categoryDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@RequestHeader("X-User-ID") String userId, @PathVariable @NotNull(message = "板块ID不能为空") Integer id) {
        userService.checkAdminPermission(userId);
        categoryService.deleteCategory(id);
    }

    @PutMapping("/{id}")
    public void updateCategory(@RequestHeader("X-User-ID") String userId, @PathVariable @NotNull(message = "板块ID不能为空") Integer id, @RequestBody @Validated CategoryDTO categoryDTO) {
        userService.checkAdminPermission(userId);
        categoryService.updateCategory(id, categoryDTO);
    }

    @GetMapping("/{id}")
    public Category getCategory(@PathVariable @NotNull(message = "板块ID不能为空") Integer id) {
        return categoryService.getCategory(id);
    }

    @GetMapping("/list")
    public Pagination<Category> getCategoryList(@RequestParam(name = "pageNum", defaultValue = "1") Long pageNum,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Long pageSize) {
        return Pagination.of(categoryService.page(new Page<>(pageNum, pageSize)));
    }
}