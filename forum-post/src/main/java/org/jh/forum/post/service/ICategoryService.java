package org.jh.forum.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jh.forum.post.dto.CategoryDTO;
import org.jh.forum.post.model.Category;

public interface ICategoryService extends IService<Category> {
    void addCategory(CategoryDTO categoryDTO);

    void deleteCategory(Integer id);

    void updateCategory(Integer id, CategoryDTO categoryDTO);

    Category getCategory(Integer id);

    Category ensureCategoryExist(Integer id);
}