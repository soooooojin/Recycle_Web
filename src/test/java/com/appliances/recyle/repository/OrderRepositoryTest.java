package com.appliances.recyle.repository;


import com.appliances.recyle.domain.Item;
import com.appliances.recyle.domain.Member;
import com.appliances.recyle.domain.Order;
import com.appliances.recyle.domain.Pay;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

//    @Autowired
//    Pay

    @Test
    public void InsertOrder() {

        Member member = Member.builder()
                .email("lsj@gmail.com")
                .build();
        member = memberRepository.save(member);

        Item item = Item.builder()
                .ino(1L)
                .build();
        item = itemRepository.save(item);

//        Pay pay = Pay.builder()
//                .pno("가격임")
//                .build();


        Order order = Order.builder()
                .ono(1L)
                .member(member)
                .item(item)
//                .pay(pay)
                .purl("본인이 찍은 제품")
                .ostatus("진행상태 확인")
                .oaddress("서면임당")
                .build();

        Order result = orderRepository.save(order);

        log.info("더미 데이터 확인 중 " + result);

    }

    @Test
    @Transactional
    public void TestOrder() {
        Optional<Order> result = orderRepository.findById(1L);
        Order order = result.get();
        log.info("Order : " + order);
    }

}
