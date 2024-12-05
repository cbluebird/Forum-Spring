package org.jh.forum.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jh.forum.post.model.Category;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}