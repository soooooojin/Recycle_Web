package com.appliances.recyle.service;

import com.appliances.recyle.domain.Item;
import com.appliances.recyle.dto.ItemDTO;
import com.appliances.recyle.dto.PageRequestDTO;
import com.appliances.recyle.dto.PageResponseDTO;
import com.appliances.recyle.repository.ItemRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Log4j2
public class ItemServiceTests {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void testInsert() {
        ItemDTO itemDTO = ItemDTO.builder()
                .iname("업소용냉장고")
                .iprice(15000L)
                .build();

        itemService.insert(itemDTO);
    }

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<ItemDTO> responseDTO = itemService.productList(pageRequestDTO);
        log.info("list 테스트 responseDTO : " + responseDTO);

    }

    @Test
    public void read() {
        Optional<Item> result = itemRepository.findByIname("통돌이세탁기");
        log.info("알려줭 : "+result);
    }

    @Test
    public void getItemByName(){

    }

}
