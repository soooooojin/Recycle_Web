package com.appliances.recyle.repository;

import com.appliances.recyle.domain.Item;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Log4j2
public class ItemRepositoryTests {

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void insertItem() {
        List<Item> items = Arrays.asList(
                Item.builder()
                        .iname("가정용냉장고")
                        .iprice(12000L)
                        .build(),
                Item.builder()
                        .iname("업소용냉장고")
                        .iprice(15000L)
                        .build(),
                Item.builder()
                        .iname("통돌이세탁기")
                        .iprice(3000L)
                        .build(),
                Item.builder()
                        .iname("드럼세탁기")
                        .iprice(5000L)
                        .build(),
                Item.builder()
                        .iname("벽걸이에어컨")
                        .iprice(3000L)
                        .build(),
                Item.builder()
                        .iname("스탠드에어컨")
                        .iprice(5000L)
                        .build(),
                Item.builder()
                        .iname("TV")
                        .iprice(5000L)
                        .build(),
                Item.builder()
                        .iname("전자레인지")
                        .iprice(0L)
                        .build(),
                Item.builder()
                        .iname("CPU")
                        .iprice(0L)
                        .build(),
                Item.builder()
                        .iname("그래픽카드")
                        .iprice(0L)
                        .build(),
                Item.builder()
                        .iname("메인보드")
                        .iprice(0L)
                        .build(),
                Item.builder()
                        .iname("파워")
                        .iprice(0L)
                        .build(),
                Item.builder()
                        .iname("램")
                        .iprice(0L)
                        .build()
        );

        itemRepository.saveAll(items);
    }

    @Test
    public void testPaging() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("ino").descending());

        Page<Item> result = itemRepository.findAll(pageable);

        log.info("전체 갯수 total  result.getTotalElements() : " + result.getTotalElements());
        log.info("전체 페이지  result.getTotalPages() : " + result.getTotalPages());
        log.info("페이지 number  result.getNumber() : " + result.getNumber());
        log.info("페이지 당 불러올 수  result.getSize() : " + result.getSize());
        log.info("불러올 데이터 목록  result.getContent() : " + result.getContent());

        List<Item> list = result.getContent();
        list.forEach(item -> log.info(item));
    }

}
