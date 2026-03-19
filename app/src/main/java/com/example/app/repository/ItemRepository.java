package com.example.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app.model.entity.ItemModelo;

public interface ItemRepository extends JpaRepository<ItemModelo, Long>{
    
}
