package org.jh.forum.post.model;

import lombok.Data;

@Data
public class PostTag {
    private Long id;
    private Long postId;
    private Long tagId;
}