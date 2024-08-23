package com.appliances.recyle.repository;


import com.appliances.recyle.domain.Item;
import com.appliances.recyle.domain.Member;
import com.appliances.recyle.domain.MemberRole;
import com.appliances.recyle.dto.ItemDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class ItemRepositoryTests {

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void insertItem() {
        Item item = Item.builder()
                .iname("통돌이 세탁기")
                .price(4500L)
                .build();

        itemRepository.save(item);
    }

}


