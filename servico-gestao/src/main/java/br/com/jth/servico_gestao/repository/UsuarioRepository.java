package br.com.jth.servico_gestao.repository;

import br.com.jth.servico_gestao.enums.usuario.Cargo;
import br.com.jth.servico_gestao.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, UUID> {
    @Query("SELECT DISTINCT u FROM UsuarioModel u JOIN u.cargos c WHERE u.ativo = true AND c = :cargo")
    List<UsuarioModel> findByAtivoTrueAndCargo(@Param("cargo") Cargo cargo);

    List<UsuarioModel> findByAtivoTrue();

    boolean existsByEmail(String email);
}