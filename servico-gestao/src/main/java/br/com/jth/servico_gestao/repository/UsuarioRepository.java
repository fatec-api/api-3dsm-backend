package br.com.jth.servico_gestao.repository;

import br.com.jth.servico_gestao.enums.usuario.Cargo;
import br.com.jth.servico_gestao.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, UUID> {

    List<UsuarioModel> findByAtivoTrueAndCargo(Cargo cargo);
    List<UsuarioModel> findByAtivoTrue();
    boolean existsByEmail(String email);
}
