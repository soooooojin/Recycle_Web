package com.appliances.recyle.controller;

import com.appliances.recyle.domain.Item;
import com.appliances.recyle.domain.Order;
import com.appliances.recyle.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public void orderGet(Model model) {
        model.addAttribute("member", new Order());
    }

    @PostMapping("/order")
    public void orderPost(@ModelAttribute Order order) {
        // Order 객체를 저장합니다. 예시로 orderService를 사용하여 저장
        orderService.save(order);
        log.info("주문 저장: " + order);
    }

//    @PostMapping("/save")
//    public ResponseEntity<Void> saveItems(@RequestBody List<Item> items) {
////        orderService.saveAll(items);
//        log.info("쿠키 확인 : "+items);
//        return ResponseEntity.ok().build();
//    }

    @GetMapping("/pay")
    public void payGet() {

    }

}
