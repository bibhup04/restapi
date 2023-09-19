package com.learning.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.learning.restapi.entity.Cycle;

public interface CycleRepository extends JpaRepository<Cycle, Long>{
    
}
