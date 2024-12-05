package org.jh.forum.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jh.forum.user.model.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}