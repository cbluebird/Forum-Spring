package org.jh.post.service.userService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user")
public interface SchedualServiceHi {

    @RequestMapping(value = "/user/login", method = RequestMethod.GET)
    String sayHiFromClientOne(@RequestParam("name") String name);
}