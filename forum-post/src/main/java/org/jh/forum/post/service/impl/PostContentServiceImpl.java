package org.jh.forum.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jh.forum.post.mapper.PostContentMapper;
import org.jh.forum.post.model.PostContent;
import org.jh.forum.post.service.IPostContentService;
import org.springframework.stereotype.Service;

@Service
public class PostContentServiceImpl extends ServiceImpl<PostContentMapper, PostContent> implements IPostContentService {
}