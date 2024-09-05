package com.appliances.recyle.controller;

import com.appliances.recyle.domain.Member;
import com.appliances.recyle.dto.OrderItemDTO;
import com.appliances.recyle.repository.ItemRepository;
import com.appliances.recyle.repository.MemberRepository;
import com.appliances.recyle.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Log4j2
@Controller
@RequestMapping("/echopickup/product")
@RequiredArgsConstructor
public class OrderController {


    private final OrderService orderService;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
//    private final PaymentRepository paymentRepository;

    @GetMapping
    public String productGet() {
        return "/echopickup/product/product";
    }

    @GetMapping("/order")
    public void orderGet(@AuthenticationPrincipal UserDetails user, Model model) {
        Optional<Member> optionalMember = memberRepository.findByEmail(user.getUsername());
        Member member = optionalMember.get(); // Optional에서 Member 객체 추출
        log.info("member 화면 확인: " + member);

        model.addAttribute("member", member);
    }

    @PostMapping("/order")
    public ResponseEntity<Void> saveOrder(@RequestBody List<OrderItemDTO> orders,
                                          @AuthenticationPrincipal UserDetails user) {
        log.info("POST /order 진입");
        String email = user.getUsername();  // 로그인한 사용자 이메일 가져오기

        for (OrderItemDTO orderItemDTO : orders) {
            orderService.saveOrder(email, orderItemDTO);
            log.info("나오긴 하니"+orderItemDTO);
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/pay")
    public void payGet() {

    }

}
