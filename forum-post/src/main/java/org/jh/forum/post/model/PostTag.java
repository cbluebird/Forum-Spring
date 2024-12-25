package org.jh.forum.post.model;

import lombok.Data;

@Data
public class PostTag {
    private Integer id;
    private Integer postId;
    private Integer tagId;
}