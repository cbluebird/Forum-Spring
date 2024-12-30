package org.jh.forum.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TagDTO {
    @NotBlank(message = "标签名称不能为空")
    private String name;
}