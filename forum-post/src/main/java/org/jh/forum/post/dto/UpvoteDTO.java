package org.jh.forum.post.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpvoteDTO {
    @NotNull(message = "点赞对象ID不能为空")
    private Integer id;
}
