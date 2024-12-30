package org.jh.forum.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.jh.forum.post.constant.Visibility;

import java.util.Set;

@Data
public class PostDTO {
    @NotNull(message = "板块ID不能为空")
    private Integer categoryId;
    @NotBlank(message = "标题不能为空")
    private String title;
    @NotNull(message = "内容不能为空")
    private String content;
    @NotNull(message = "可见性不能为空")
    private Visibility visibility;
    private String ip;
    private String ipLoc;
    private Set<Integer> tagIds;
}