package org.jh.user.controller.userController;

import org.jh.user.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private DemoService demoService;

    @RequestMapping("/login")
    public String home(@RequestParam(value = "name", defaultValue = "forezp") String name) {
        demoService.log();
        return "hi " + name + " ,i am from port:" + "11111";
    }

}