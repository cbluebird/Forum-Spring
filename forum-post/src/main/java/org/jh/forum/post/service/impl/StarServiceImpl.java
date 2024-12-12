package org.jh.forum.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jh.forum.post.mapper.StarMapper;
import org.jh.forum.post.model.Star;
import org.jh.forum.post.service.IStarService;
import org.springframework.stereotype.Service;

@Service
public class StarServiceImpl extends ServiceImpl<StarMapper, Star> implements IStarService {
}