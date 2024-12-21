package org.jh.forum.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jh.forum.post.model.PostTag;
import org.jh.forum.post.model.Tag;

import java.util.List;

public interface ITagService extends IService<Tag> {
    public List<Tag> getTagsByPostTags(List<PostTag> postTags);
}
