package org.jh.forum.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jh.forum.post.mapper.TagMapper;
import org.jh.forum.post.model.PostTag;
import org.jh.forum.post.model.Tag;
import org.jh.forum.post.service.ITagService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    @Override
    public List<Tag> getTagsByPostTags(List<PostTag> postTags) {
        List<Tag> tags = new ArrayList<>();
        for (PostTag postTag : postTags) {
            Tag tag = this.baseMapper.selectById(postTag.getTagId());
            if (tag == null) {
                continue;
            }
            tags.add(tag);
        }
        return tags;
    }
}