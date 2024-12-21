package org.jh.forum.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jh.forum.post.mapper.PostTagMapper;
import org.jh.forum.post.model.PostTag;
import org.jh.forum.post.service.IPostTagService;
import org.springframework.stereotype.Service;

@Service
public class PostTagServiceImpl extends ServiceImpl<PostTagMapper, PostTag> implements IPostTagService {
}