package com.example.app.repository;

import com.example.app.model.entity.ProjetoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app.model.entity.ItemModel;

import java.util.List;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<ItemModel, Long>{
    long countByProjetoModel(ProjetoModel projetoModel);
    List<ItemModel> findByProjetoModelId(Long projetoId);
    List<ItemModel> findByUsuarioModelId(UUID usuarioId);
}
