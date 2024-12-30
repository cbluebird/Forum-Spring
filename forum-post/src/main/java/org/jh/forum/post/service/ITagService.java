package org.jh.forum.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jh.forum.common.api.Pagination;
import org.jh.forum.post.dto.TagDTO;
import org.jh.forum.post.model.Tag;
import org.jh.forum.post.vo.TagVO;

public interface ITagService extends IService<Tag> {
    void addTag(String userId, TagDTO tagDTO);

    TagVO getTag(Integer id);

    Pagination<TagVO> getHotTagOfDay(Long pageNum, Long pageSize);

    Tag ensureTagExist(Integer id);
}