package com.learning.restapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.restapi.entity.Cart;
import com.learning.restapi.entity.Cycle;

public interface CartRepository extends JpaRepository<Cart, Long> {
    
    List<Cycle> findByUser_Id(long userId);
}
