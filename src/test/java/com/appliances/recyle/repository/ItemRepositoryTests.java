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

import java.util.List;

@SpringBootTest
@Log4j2
public class ItemRepositoryTests {

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void insertItem() {
        Item item = Item.builder()
                .iname("가정용냉장고")
                .iprice(12000L)
                .build();

        itemRepository.save(item);
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
