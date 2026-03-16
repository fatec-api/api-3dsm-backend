package com.example.App.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.App.Model.Entity.ItemModelo;

public interface ItemRepository extends JpaRepository<ItemModelo, Long>{
    
}
