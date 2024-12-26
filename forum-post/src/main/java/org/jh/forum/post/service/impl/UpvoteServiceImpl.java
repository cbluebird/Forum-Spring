package org.jh.forum.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jh.forum.post.mapper.UpvoteMapper;
import org.jh.forum.post.model.Upvote;
import org.jh.forum.post.service.IUpvoteService;
import org.springframework.stereotype.Service;

@Service
public class UpvoteServiceImpl extends ServiceImpl<UpvoteMapper, Upvote> implements IUpvoteService {
}