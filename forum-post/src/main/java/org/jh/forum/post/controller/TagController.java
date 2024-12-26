package org.jh.forum.post.controller;

import org.jh.forum.post.model.Tag;
import org.jh.forum.post.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/post/tag")
public class TagController {
    @Autowired
    private ITagService tagService;

    @PostMapping("/add")
    public void addTag(@RequestBody Map<String, String> requestBody, @RequestHeader("X-User-ID") String userId) {
        String tagName = requestBody.get("name");
        if (tagName != null && !tagName.isEmpty()) {
            Tag tag = new Tag();
            tag.setUserId(Integer.valueOf(userId));
            tag.setTag(tagName);
            tagService.save(tag);
        }
    }
}
