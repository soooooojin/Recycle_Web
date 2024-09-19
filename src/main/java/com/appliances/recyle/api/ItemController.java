package com.appliances.recyle.api;
import com.appliances.recyle.domain.Item;
import com.appliances.recyle.dto.ItemDTO;
import com.appliances.recyle.service.ItemService2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
public class ItemController {

    private final ItemService2 itemService2;

    public ItemController(ItemService2 itemService2) {
        this.itemService2 = itemService2;
    }

    @GetMapping("/api/getAllItems")
    public ResponseEntity<List<ItemDTO>> getAllItems() {
        List<Item> items = itemService2.getAllItems();
        List<ItemDTO> itemDTOs = items.stream()
                .map(item -> new ItemDTO(item.getIno(), item.getIname(), item.getIprice()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(itemDTOs, HttpStatus.OK);
    }

//    @GetMapping("/api/getItems")
//    public ResponseEntity<List<ItemDTO>> getItems() {
//        List<Item> items = itemService2.getAllItems();
//        List<ItemDTO> itemDTOs = items.stream()
//                .map(item -> new ItemDTO(item.getIno(), item.getIname(), item.getIprice(), item.getImageUrl()))
//                .collect(Collectors.toList());
//        return new ResponseEntity<>(itemDTOs, HttpStatus.OK);
//    }
}
