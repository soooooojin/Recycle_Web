package com.appliances.recyle.service;

import com.appliances.recyle.domain.Item;
import com.appliances.recyle.dto.ItemDTO;
import com.appliances.recyle.dto.PageRequestDTO;
import com.appliances.recyle.dto.PageResponseDTO;
import com.appliances.recyle.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ItemServicelmpl implements ItemService{

    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

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
        Optional<Item> items = itemRepository.findByIname(iname);
        return items.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Long insert(ItemDTO itemDTO) {
        Item item = dtoToEntity(itemDTO);
        Long ino = itemRepository.save(item).getIno();
        return ino;
    }

    @Override
    public PageResponseDTO<ItemDTO> productList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("ino");

        Page<Item> result = itemRepository.findAll(pageable);

        List<ItemDTO> itemList = result.getContent().stream()
                .map(product -> modelMapper.map(product,ItemDTO.class))
                .collect(Collectors.toList());

        PageResponseDTO pageResponseDTO = PageResponseDTO.<ItemDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .itemList(itemList)
                .total((int) result.getTotalElements())
                .build();

        return pageResponseDTO;
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
