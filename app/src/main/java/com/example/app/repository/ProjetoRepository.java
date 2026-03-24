package com.example.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app.model.entity.ProjetoModel;

public interface  ProjetoRepository extends JpaRepository<ProjetoModel, Long> {
    
}
