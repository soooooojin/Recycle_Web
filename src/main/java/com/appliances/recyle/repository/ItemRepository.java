package com.appliances.recyle.repository;


import com.appliances.recyle.domain.Item;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

//    Optional<Item> findByItem(@NotEmpty String iname, Long iprice);

    @Query("select i from Item i WHERE i.iname = :iname")
    List<Item> findByItemName(String iname);
}
