package com.example.app.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app.model.entity.UsuarioModel;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, UUID>{
    
}
