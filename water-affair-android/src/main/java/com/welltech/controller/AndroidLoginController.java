package com.welltech.controller;

import com.welltech.service.AndroidUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class AndroidLoginController {

    @Autowired
    public AndroidUserService userService;

    @RequestMapping(value = {"/android/login"},method = RequestMethod.POST)
    @ResponseBody
    public boolean androidLogin(@RequestBody Map<String,String> map){

        String name = map.get("name");
        String password = map.get("password");
        System.out.println(name+":"+password);

        return userService.isUser(name,password);
    }
}
