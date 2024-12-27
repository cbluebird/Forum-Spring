package org.jh.forum.auth.feign;

import org.jh.forum.auth.dto.UserFeignDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "forum-user", path = "/api/user")
public interface UserFeign {
    @GetMapping("/username/{username}")
    UserFeignDTO getUserByUsername(@PathVariable("username") String username);
}