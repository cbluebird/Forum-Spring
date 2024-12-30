package org.jh.forum.post.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jh.forum.common.api.ErrorCode;
import org.jh.forum.common.exception.BizException;
import org.jh.forum.common.exception.SystemException;
import org.jh.forum.post.dto.CategoryDTO;
import org.jh.forum.post.mapper.CategoryMapper;
import org.jh.forum.post.model.Category;
import org.jh.forum.post.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        if (categoryMapper.selectOne(new QueryWrapper<Category>().eq("name", categoryDTO.getName())) != null) {
            throw new BizException(ErrorCode.POST_CATEGORY_ALREADY_EXIST, "板块名称: " + categoryDTO.getName() + " 已存在");
        }
        Category category = new Category();
        BeanUtil.copyProperties(categoryDTO, category);
        if (categoryMapper.insert(category) != 1) {
            throw new SystemException(ErrorCode.DB_MYSQL_ERROR, "添加板块: " + category + " 失败");
        }
    }

    @Override
    public void deleteCategory(Integer id) {
        ensureCategoryExist(id);
        if (categoryMapper.deleteById(id) != 1) {
            throw new SystemException(ErrorCode.DB_MYSQL_ERROR, "删除板块ID: " + id + " 失败");
        }
    }

    @Override
    public void updateCategory(Integer id, CategoryDTO categoryDTO) {
        Category category = ensureCategoryExist(id);
        if (!category.getName().equals(categoryDTO.getName()) && categoryMapper.selectOne(new QueryWrapper<Category>().eq("name", categoryDTO.getName())) != null) {
            throw new BizException(ErrorCode.POST_CATEGORY_ALREADY_EXIST, "板块名称: " + categoryDTO.getName() + " 已存在");
        }
        BeanUtil.copyProperties(categoryDTO, category);
        if (categoryMapper.updateById(category) != 1) {
            throw new SystemException(ErrorCode.DB_MYSQL_ERROR, "更新板块ID: " + id + " 失败");
        }
    }

    @Override
    public Category getCategory(Integer id) {
        return ensureCategoryExist(id);
    }

    @Override
    public Category ensureCategoryExist(Integer id) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BizException(ErrorCode.POST_CATEGORY_NOT_FOUND, "板块ID: " + id + " 不存在");
        }
        return category;
    }
}