package com.appliances.recyle.service;



import com.appliances.recyle.domain.Item;
import com.appliances.recyle.domain.Order;
import com.appliances.recyle.dto.OrderDTO;

import java.util.List;

public interface OrderService {

    List<OrderDTO> getOrders();
    OrderDTO read(Long ono);

    OrderDTO entityToDTO(Order order);

    List<OrderDTO> searchOrders(String keyword);

    Long insert(OrderDTO orderDTO);

    Order dtoTOEntity(OrderDTO orderDTO);

    void update(OrderDTO orderDTO);
    void delete(OrderDTO orderDTO);

    void save(Order order);
    void saveAll(List<Item> items);
}
