package org.jh.forum.post.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jh.forum.common.exception.BizException;
import org.jh.forum.post.dto.CategoryDTO;
import org.jh.forum.post.model.Category;
import org.jh.forum.post.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CategoryServiceTest {

    @Autowired
    private CategoryServiceImpl categoryService;

    @Test
    public void testAddCategory() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Test Category");
        categoryDTO.setDescription("This is a test category.");
        categoryService.addCategory(categoryDTO);
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<Category>().eq("name", "Test Category");
        Category category = categoryService.getOne(categoryQueryWrapper);
        assertNotNull(category);
        assertEquals("Test Category", category.getName());
    }

    @Test
    public void testGetCategory() {
        Category category = categoryService.getCategory(1);
        assertNotNull(category);
        assertEquals(1, category.getId());
    }

    @Test
    public void testDeleteCategory() {
        categoryService.deleteCategory(1);
        assertThrows(BizException.class, () -> categoryService.getCategory(1));
    }

    @Test
    public void testUpdateCategory() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Updated Category");
        categoryDTO.setDescription("This is an updated category.");
        categoryService.updateCategory(1, categoryDTO);
        Category category = categoryService.getCategory(1);
        assertNotNull(category);
        assertEquals("Updated Category", category.getName());
    }

    @Test
    public void testEnsureCategoryExist() {
        Category category = categoryService.ensureCategoryExist(1);
        assertNotNull(category);
        assertEquals(1, category.getId());
    }
}