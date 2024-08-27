package com.appliances.recyle.service;

import com.appliances.recyle.domain.Item;
import com.appliances.recyle.dto.ItemDTO;
import com.appliances.recyle.dto.PageRequestDTO;
import com.appliances.recyle.dto.PageResponseDTO;

import java.util.List;

public interface ItemService {

    List<ItemDTO> getAllItems();
    ItemDTO read(Long ino);
    //변환
    ItemDTO entityToDTO(Item item);

    List<ItemDTO> searchItems(String keyword);

    //히지작업 / 필요없을지 모르겠지만 일단 넣어둠
    Long insert(ItemDTO itemDTO);
    PageResponseDTO<ItemDTO> productList(PageRequestDTO pageRequestDTO);
    Item dtoToEntity(ItemDTO itemDTO);

    void update(ItemDTO itemDTO);
    void delete(Long ino);






}
