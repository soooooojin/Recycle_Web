package com.appliances.recyle.controller;


import com.appliances.recyle.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("/echopickup")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/product")
    public void product() {

    }


}
