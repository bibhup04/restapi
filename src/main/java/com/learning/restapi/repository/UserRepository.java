package com.learning.restapi.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.learning.restapi.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name); 
    Integer countByName(String name);
    @Query(value = "select * from user where name = ?1", nativeQuery = true)
    User findBySomeConstraintSpringCantParse(String name);
    
}
