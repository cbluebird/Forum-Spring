package org.jh.forum.post.feign;

import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "forum-user", path = "/api/user")
public interface UserFeign {
    @GetMapping("/id/{userId}")
    Object getUserById(@PathVariable("userId") @NotNull(message = "User ID 不能为空") Long userId);
}
