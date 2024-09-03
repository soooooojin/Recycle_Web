package com.appliances.recyle.controller;

import com.appliances.recyle.domain.Item;
import com.appliances.recyle.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/echopickup/product")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public String productGet() {
        return "/echopickup/product/product";
    }

    @GetMapping("/order")
    public void orderGet() {

    }

    @PostMapping("/save")
    public ResponseEntity<Void> saveItems(@RequestBody List<Item> items) {
        orderService.saveAll(items);
        log.info("쿠키 확인 : "+items);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pay")
    public void payGet() {

    }

}
