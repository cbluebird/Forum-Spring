package org.jh.forum.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jh.forum.post.mapper.CollectionMapper;
import org.jh.forum.post.model.Collection;
import org.jh.forum.post.service.ICollectionService;
import org.springframework.stereotype.Service;

@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, Collection> implements ICollectionService {
}