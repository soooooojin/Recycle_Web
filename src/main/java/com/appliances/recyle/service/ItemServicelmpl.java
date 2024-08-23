package com.appliances.recyle.service;

import com.appliances.recyle.domain.Item;
import com.appliances.recyle.dto.ItemDTO;
import com.appliances.recyle.repository.ItemRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ItemServicelmpl implements ItemService{

    @Autowired
    private ItemRepository itemRepository;


    @Override
    public List<ItemDTO> getAllItems() {
        return itemRepository.findAll().stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDTO read(Long ino) {
        return itemRepository.findById(ino)
                .map(this::entityToDTO)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    @Override
    public ItemDTO entityToDTO(Item item) {

        ItemDTO itemDTO = ItemDTO.builder()
                .ino(item.getIno())
                .iname(item.getIname())
                .iprice(item.getIprice())
                .build();

        //이미지 추가?

        return itemDTO;
    }

    @Override
    public List<ItemDTO> searchItems(String iname) { // 일단 iname으로 설정.
        List<Item> items = itemRepository.findByItemName(iname);
        return items.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Long insert(ItemDTO itemDTO) {
        Item item = dtoToEntity(itemDTO);
        Long ino =itemRepository.save(item).getIno();
        return null;
    }

    @Override
    public Item dtoToEntity(ItemDTO itemDTO) {

        Item item = Item.builder()
                .ino(itemDTO.getIno())
                .iname(itemDTO.getIname())
                .iprice(itemDTO.getIprice())
                .build();

        //첨부 이미지는 purl로 할건가?

        return item;
    }

    @Override
    public void update(ItemDTO itemDTO) {
        Item item = dtoToEntity(itemDTO);
        itemRepository.save(item);
    }

    @Override
    public void delete(Long ino) {
        itemRepository.deleteById(ino);

    }
}
