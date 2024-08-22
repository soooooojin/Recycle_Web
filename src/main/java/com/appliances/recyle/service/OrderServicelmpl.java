package com.appliances.recyle.service;

import com.appliances.recyle.domain.Item;
import com.appliances.recyle.domain.Order;
import com.appliances.recyle.dto.OrderDTO;
import com.appliances.recyle.repository.ItemRepository;
import com.appliances.recyle.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class OrderServicelmpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    // @Autowired ::final:더 이상 바꾸지 않음
    //    private final TypeRepository typeRepository;

    @Autowired
    public OrderServicelmpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    //  this.payRepository = payRepository;
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
                .iprice(order.getItem().getIprice())
                .purl(order.getPurl())
                .oaddress(order.getOaddress())
                .pmethod(order.getPay().getPmethod())
                .build();

//        List<String> images = order.getPurl().stream()
//                .map(image -> image.get);
// 사진올라가는게 여긴가?

        return orderDTO;
    }


    // 있어야할까? 히지가 필요할지도?
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
        //등록할 떄 이름으로 하나?
        Item item = itemRepository.findById(orderDTO.getIprice())
                .orElseGet(() -> {
                    Item newitem = Item.builder()
                            .iname(orderDTO.getIname())
                            //사진도 필요하지 않아?
                            .build();

                    return itemRepository.save(newitem);
                });

        Order order = Order.builder()
                .ono(orderDTO.getOno())
                .
                .build();

        return null;
    }

    @Override
    public void update(OrderDTO orderDTO) {

    }

    @Override
    public void delete(OrderDTO orderDTO) {

    }


}
