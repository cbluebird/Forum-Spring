package org.jh.forum.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UserBatchDTO {
    @NotNull(message = "用户 ID 列表不能为空")
    List<Long> userIds;
}
