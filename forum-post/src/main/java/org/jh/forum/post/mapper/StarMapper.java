package org.jh.forum.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jh.forum.post.model.Star;

@Mapper
public interface StarMapper extends BaseMapper<Star> {
}