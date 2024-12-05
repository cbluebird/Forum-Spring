package org.jh.forum.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDTO {
    @NotBlank(message = "分类名称不能为空")
    private String name;
    @NotBlank(message = "分类描述不能为空")
    private String description;
}