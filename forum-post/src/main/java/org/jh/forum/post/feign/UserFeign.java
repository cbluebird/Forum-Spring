package org.jh.forum.post.feign;

import jakarta.validation.constraints.NotNull;
import org.jh.forum.post.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "forum-user", path = "/api/user")
public interface UserFeign {
    @GetMapping("/id/{userId}")
    UserDTO getUserById(@PathVariable("userId") @NotNull(message = "用户ID不能为空") Integer userId);

    @PostMapping("/batch-get")
    List<UserDTO> getUserByIds(@RequestBody List<Integer> ids);
}