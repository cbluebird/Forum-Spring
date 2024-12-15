package org.jh.forum.post.dto;

import lombok.Data;
import org.jh.forum.post.constant.PostContentType;

@Data
public class PostContentDTO {
    private String content;
    private PostContentType type;
}
