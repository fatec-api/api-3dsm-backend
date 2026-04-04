package com.example.app.repository;

import com.example.app.model.entity.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, UUID> {
    boolean existsByEmail(String email);
    List<UsuarioModel> findByAtivoTrueAndCargo(UsuarioModel.Cargo cargo);
}
