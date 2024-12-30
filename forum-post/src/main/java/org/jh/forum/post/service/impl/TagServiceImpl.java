package org.jh.forum.post.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jh.forum.common.api.ErrorCode;
import org.jh.forum.common.api.Pagination;
import org.jh.forum.common.exception.BizException;
import org.jh.forum.common.exception.SystemException;
import org.jh.forum.post.constant.RedisKey;
import org.jh.forum.post.dto.TagDTO;
import org.jh.forum.post.mapper.TagMapper;
import org.jh.forum.post.model.Tag;
import org.jh.forum.post.service.ITagService;
import org.jh.forum.post.vo.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    @Override
    public void addTag(String userId, TagDTO tagDTO) {
        if (tagMapper.selectOne(new QueryWrapper<Tag>().eq("name", tagDTO.getName())) != null) {
            throw new BizException(ErrorCode.POST_TAG_ALREADY_EXIST, "标签名称: " + tagDTO.getName() + " 已存在");
        }
        Tag tag = new Tag();
        BeanUtil.copyProperties(tagDTO, tag);
        tag.setUserId(Integer.valueOf(userId));
        if (tagMapper.insert(tag) != 1) {
            throw new SystemException(ErrorCode.DB_MYSQL_ERROR, "添加标签: " + tag + " 失败");
        }
    }

    @Override
    public TagVO getTag(Integer id) {
        Tag tag = ensureTagExist(id);
        TagVO tagVO = new TagVO();
        BeanUtil.copyProperties(tag, tagVO);
        return tagVO;
    }

    @Override
    public Pagination<TagVO> getHotTagOfDay(Long pageNum, Long pageSize) {
        Set<Integer> tagIds = Optional.ofNullable(
                redisTemplate.opsForZSet().reverseRange(RedisKey.HOT_TAG_DAY, (pageNum - 1) * pageSize, pageNum * pageSize - 1)
        ).orElse(Collections.emptySet());
        if (tagIds.isEmpty()) {
            return Pagination.of(Collections.emptyList(), pageNum, pageSize, 0L);
        }
        List<Tag> tagList = tagMapper.selectByIds(tagIds);
        List<TagVO> tagVOList = tagList.stream().map(tag -> {
            TagVO tagVO = new TagVO();
            BeanUtil.copyProperties(tag, tagVO);
            return tagVO;
        }).toList();
        return Pagination.of(tagVOList, pageNum, pageSize, redisTemplate.opsForZSet().zCard(RedisKey.HOT_TAG_DAY));
    }

    @Override
    public Tag ensureTagExist(Integer id) {
        Tag tag = tagMapper.selectById(id);
        if (tag == null) {
            throw new BizException(ErrorCode.POST_TAG_NOT_FOUND, "标签ID: " + id + " 不存在");
        }
        return tag;
    }
}