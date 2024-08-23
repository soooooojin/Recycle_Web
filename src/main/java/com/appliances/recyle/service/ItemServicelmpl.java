package com.appliances.recyle.service;

import com.appliances.recyle.domain.Item;
import com.appliances.recyle.dto.ItemDTO;
import com.appliances.recyle.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class ItemServicelmpl implements ItemService{

    private ItemRepository itemRepository;

    @Override
    public List<ItemDTO> getAllItems() {
        return List.of();
    }

    @Override
    public ItemDTO read(Long ino) {
        return null;
    }

    @Override
    public ItemDTO entityToDTO(Item item) {
        return null;
    }

    @Override
    public List<ItemDTO> searchItems(String keyword) {
        return List.of();
    }

    @Override
    public Long insert(ItemDTO itemDTO) {
        Item item = dtoToEntity(itemDTO);
        Long ino = itemRepository.save(item).getIno();
        return ino;
    }

    @Override
    public Item dtoToEntity(ItemDTO itemDTO) {
        Item item = Item.builder()
                .ino(itemDTO.getIno())
                .iname(itemDTO.getIname())
                .price(itemDTO.getIprice())
                .build();

        return item;
    }

    @Override
    public void update(ItemDTO itemDTO) {

    }

    @Override
    public void delete(Long ino) {

    }
}
