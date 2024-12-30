package org.jh.forum.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReplyDTO {
    @NotNull(message = "回复帖子ID不能为空")
    private Integer postId;
    @NotNull(message = "root不能为空")
    private Integer root;
    @NotNull(message = "parent不能为空")
    private Integer parent;
    @NotBlank(message = "content不能为空")
    private String content;
    private String ip;
    private String ipLoc;
}