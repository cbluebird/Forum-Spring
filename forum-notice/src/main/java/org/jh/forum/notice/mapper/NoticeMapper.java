package org.jh.forum.notice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jh.forum.notice.model.Notice;

@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {
}