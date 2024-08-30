package com.appliances.recyle.repository;


import com.appliances.recyle.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i WHERE i.iname = :iname")
    List<Item> findByItemName(String iname);

    List<Item> findAll();
}
