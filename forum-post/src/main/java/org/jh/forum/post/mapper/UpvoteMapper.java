package org.jh.forum.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jh.forum.post.model.Upvote;

@Mapper
public interface UpvoteMapper extends BaseMapper<Upvote> {
}