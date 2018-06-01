package com.xianjinxia.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckController extends BaseController{

    @GetMapping("/")
    public void service() {

    }
}
