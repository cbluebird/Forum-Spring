package org.jh.forum.notice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jh.forum.notice.mapper.NoticeMapper;
import org.jh.forum.notice.model.Notice;
import org.jh.forum.notice.service.INoticeService;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {
}