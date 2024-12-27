package org.jh.forum.post.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.NotNull;
import org.jh.forum.common.api.ErrorCode;
import org.jh.forum.common.api.Pagination;
import org.jh.forum.common.exception.BizException;
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
        if (categoryService.getOne(new QueryWrapper<Category>().eq("name", categoryDTO.getName())) != null) {
            throw new BizException(ErrorCode.POST_CATEGORY_ALREADY_EXIST, "板块名称: " + categoryDTO.getName() + " 已存在");
        }
        Category category = new Category();
        BeanUtil.copyProperties(categoryDTO, category);
        categoryService.save(category);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@RequestHeader("X-User-ID") String userId, @PathVariable @NotNull(message = "板块ID不能为空") Integer id) {
        userService.checkAdminPermission(userId);
        if (categoryService.getById(id) == null) {
            throw new BizException(ErrorCode.POST_CATEGORY_NOT_FOUND, "板块ID: " + id + " 不存在");
        }
        categoryService.removeById(id);
    }

    @PutMapping("/{id}")
    public void updateCategory(@RequestHeader("X-User-ID") String userId, @PathVariable @NotNull(message = "板块ID不能为空") Integer id, @RequestBody @Validated CategoryDTO categoryDTO) {
        userService.checkAdminPermission(userId);
        Category category = categoryService.getById(id);
        if (category == null) {
            throw new BizException(ErrorCode.POST_CATEGORY_NOT_FOUND, "板块ID: " + id + " 不存在");
        }
        if (!category.getName().equals(categoryDTO.getName()) && categoryService.getOne(new QueryWrapper<Category>().eq("name", categoryDTO.getName())) != null) {
            throw new BizException(ErrorCode.POST_CATEGORY_ALREADY_EXIST, "板块名称: " + categoryDTO.getName() + " 已存在");
        }
        BeanUtil.copyProperties(categoryDTO, category);
        categoryService.updateById(category);
    }

    @GetMapping("/{id}")
    public Category getCategory(@PathVariable @NotNull(message = "板块ID不能为空") Integer id) {
        Category category = categoryService.getById(id);
        if (category == null) {
            throw new BizException(ErrorCode.POST_CATEGORY_NOT_FOUND, "板块ID: " + id + " 不存在");
        }
        return category;
    }

    @GetMapping("/list")
    public Pagination<Category> getCategoryList(@RequestParam(name = "pageNum", defaultValue = "1") Long pageNum,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Long pageSize) {
        return Pagination.of(categoryService.page(new Page<>(pageNum, pageSize)));
    }
}