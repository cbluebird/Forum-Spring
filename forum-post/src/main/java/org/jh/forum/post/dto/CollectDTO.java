package org.jh.forum.post.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CollectDTO {
    @NotNull(message = "收藏对象ID不能为空")
    private Integer id;
}
