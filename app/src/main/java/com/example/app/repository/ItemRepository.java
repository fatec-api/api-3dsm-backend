package com.example.app.repository;

import com.example.app.model.entity.ItemModel;
import com.example.app.model.entity.ProjetoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemModel, Long> {
    long countByProjetoModel(ProjetoModel projetoModel); 
}