package com.appliances.recyle.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("/echopickup")
@RequiredArgsConstructor
public class RecycleController {

    @GetMapping("/index")
    public void index() {

    }

}
