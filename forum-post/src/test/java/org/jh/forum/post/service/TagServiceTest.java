package org.jh.forum.post.service;

import org.jh.forum.common.api.Pagination;
import org.jh.forum.post.dto.TagDTO;
import org.jh.forum.post.model.Tag;
import org.jh.forum.post.service.impl.TagServiceImpl;
import org.jh.forum.post.vo.TagVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TagServiceTest {

    @Autowired
    private TagServiceImpl tagService;

    @Test
    public void testAddTag() {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setName("Test Tag");
        tagService.addTag("1", tagDTO);
        Tag tag = tagService.ensureTagExist(1);
        assertNotNull(tag);
        assertEquals("Test Tag", tag.getName());
    }

    @Test
    public void testGetTag() {
        TagVO tag = tagService.getTag(1);
        assertNotNull(tag);
        assertEquals(1, tag.getId());
    }

    @Test
    public void testGetHotTagOfDay() {
        Pagination<TagVO> hotTags = tagService.getHotTagOfDay(1L, 10L);
        assertNotNull(hotTags);
        assertFalse(hotTags.getList().isEmpty());
    }
}