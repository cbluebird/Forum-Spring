package org.jh.forum.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDTO {
    @NotBlank(message = "板块名称不能为空")
    private String name;
    @NotBlank(message = "板块描述不能为空")
    private String description;
}