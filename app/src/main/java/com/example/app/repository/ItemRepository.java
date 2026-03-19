package com.example.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app.model.entity.ItemModel;

public interface ItemRepository extends JpaRepository<ItemModel, Long>{
    
}
