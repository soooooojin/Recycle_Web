package com.appliances.recyle.service;

import com.appliances.recyle.domain.Order;
import com.appliances.recyle.dto.OrderDTO;
import com.appliances.recyle.repository.ItemRepository;
import com.appliances.recyle.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class OrderServicelmpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    // @Autowired final:더 이상 바꾸지 않음
    //    private final TypeRepository typeRepository;

    @Autowired
    public OrderServicelmpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<OrderDTO> getOrders() {
        return orderRepository.findAll().stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO read(Long ono) {
        return orderRepository.findById(ono)
                .map(this::entityToDTO)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public OrderDTO entityToDTO(Order order) {

        OrderDTO orderDTO = OrderDTO.builder()
                .ono(order.getOno())
                //           memberdb것
                .email(order.getMember().getEmail())
                .iname(order.getItem().getIname())
                .iprice(order.getItem().getPrice())
                .purl()
                .build();


        return null;
    }

    @Override
    public List<OrderDTO> searchOrders(String keyword) {
        return List.of();
    }

    @Override
    public Long insert(OrderDTO orderDTO) {
        Order order = dtoTOEntity(orderDTO);
        Long ono = orderRepository.save(order).getOno();
        return ono;
    }

    @Override
    public Order dtoTOEntity(OrderDTO orderDTO) {
        return null;
    }

    @Override
    public void update(OrderDTO orderDTO) {

    }

    @Override
    public void delete(OrderDTO orderDTO) {

    }


}
