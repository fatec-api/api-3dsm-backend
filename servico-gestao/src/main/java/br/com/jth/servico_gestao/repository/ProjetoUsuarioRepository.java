package br.com.jth.servico_gestao.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

import br.com.jth.servico_gestao.model.ProjetoModel;
import br.com.jth.servico_gestao.model.ProjetoUsuarioModel;
import br.com.jth.servico_gestao.model.UsuarioModel;

public interface ProjetoUsuarioRepository extends JpaRepository<ProjetoUsuarioModel, Long>{
    boolean existsByProjetoAndUsuario(ProjetoModel projeto, UsuarioModel usuario);
    List<ProjetoUsuarioModel> findByProjetoIdAndDataDesvinculoIsNull(Long projetoId);
    List<ProjetoUsuarioModel> findByUsuarioIdAndDataDesvinculoIsNull(UUID usuarioId);
    void deleteByProjeto(ProjetoModel projeto);
}