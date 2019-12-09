package com.zm.order.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("order/test")
    public String test(){
        return "success";
    }

    @GetMapping("order/list")
    public String list(){
        return "list";
    }

    @GetMapping("login")
    public String login(){
        return "loign";
    }
}
