package br.com.jth.servico_gestao.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.jth.servico_gestao.model.ProjetoModel;

public interface ProjetoRepository extends JpaRepository<ProjetoModel, Long> {
    @Query("SELECT p FROM ProjetoModel p LEFT JOIN FETCH p.itens WHERE p.gestor.id = :gestorId")
    List<ProjetoModel> findByGestorId(@Param("gestorId") UUID gestorId);

    List<ProjetoModel> findByProfissionalAlocadoId(UUID profissionalAlocadoId);

    @Query("SELECT p FROM ProjetoModel p LEFT JOIN FETCH p.itens WHERE p.id IN " +
           "(SELECT pu.projeto.id FROM ProjetoUsuarioModel pu " +
           "WHERE pu.usuario.id = :usuarioId AND pu.dataDesvinculo IS NULL)")
    List<ProjetoModel> findByUsuarioIdWithItens(@Param("usuarioId") UUID usuarioId);
}