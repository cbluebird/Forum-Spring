package org.jh.forum.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jh.forum.post.mapper.CategoryMapper;
import org.jh.forum.post.model.Category;
import org.jh.forum.post.service.ICategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {
}
