package com.appliances.recyle.repository;

import com.appliances.recyle.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
