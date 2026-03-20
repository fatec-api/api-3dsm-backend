package com.example.app.repository;

import com.example.app.model.entity.ProjetoModel;
import com.example.app.model.entity.ProjetoUsuarioModel;
import com.example.app.model.entity.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetoUsuarioRepository extends JpaRepository<ProjetoUsuarioModel, Long> {

    boolean existsByProjetoAndUsuario(ProjetoModel projeto, UsuarioModel usuario);
}
