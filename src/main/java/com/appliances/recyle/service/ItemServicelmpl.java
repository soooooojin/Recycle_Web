package com.appliances.recyle.service;

import com.appliances.recyle.domain.Item;
import com.appliances.recyle.dto.ItemDTO;
import com.appliances.recyle.repository.ItemRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class ItemServicelmpl implements ItemService{

    @Autowired
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
        return null;
    }

    @Override
    public Item dtoToEntity(ItemDTO itemDTO) {
        return null;
    }

    @Override
    public void update(ItemDTO itemDTO) {

    }

    @Override
    public void delete(Long ino) {

    }
}
