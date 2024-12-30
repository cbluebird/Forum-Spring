package org.jh.forum.post.controller;

import jakarta.validation.constraints.NotNull;
import org.jh.forum.common.api.Pagination;
import org.jh.forum.post.dto.TagDTO;
import org.jh.forum.post.service.ITagService;
import org.jh.forum.post.vo.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/post/tag")
public class TagController {
    @Autowired
    private ITagService tagService;

    @PostMapping
    public void addTag(@RequestHeader("X-User-ID") String userId, @RequestBody @Validated TagDTO tagDTO) {
        tagService.addTag(userId, tagDTO);
    }

    @GetMapping("/{id}")
    public TagVO getTag(@PathVariable @NotNull(message = "标签ID不能为空") Integer id) {
        return tagService.getTag(id);
    }

    @GetMapping("/hot/day")
    public Pagination<TagVO> getHotTagOfDay(@RequestParam(name = "pageNum", defaultValue = "1") Long pageNum,
                                            @RequestParam(name = "pageSize", defaultValue = "10") Long pageSize) {
        return tagService.getHotTagOfDay(pageNum, pageSize);
    }
}