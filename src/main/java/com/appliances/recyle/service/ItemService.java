package com.appliances.recyle.service;

import com.appliances.recyle.domain.Item;
import com.appliances.recyle.dto.ItemDTO;

import java.util.List;

public interface ItemService {

    List<ItemDTO> getAllItems();
    ItemDTO read(Long ino);
    //변환
    ItemDTO entityToDTO(Item item);

    List<ItemDTO> searchItems(String keyword);

    Long insert(ItemDTO itemDTO);

    Item dtoToEntity(ItemDTO itemDTO);

    void update(ItemDTO itemDTO);
    void delete(Long ino);






}
