package org.jh.forum.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jh.forum.post.model.Collection;

@Mapper
public interface CollectionMapper extends BaseMapper<Collection> {
}