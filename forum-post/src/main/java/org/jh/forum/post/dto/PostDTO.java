package org.jh.forum.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.jh.forum.post.constant.Visibility;

@Data
public class PostDTO {
    @NotNull(message = "Category ID cannot be null")
    private Long categoryId;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    private Visibility visibility;
    private String ip;
    private String ipLoc;
    private PostContentDTO[] content;
    private Long[] tags;
}