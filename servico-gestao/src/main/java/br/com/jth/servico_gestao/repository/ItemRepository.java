package br.com.jth.servico_gestao.repository;

import br.com.jth.servico_gestao.model.ItemModel;
import br.com.jth.servico_gestao.model.ProjetoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<ItemModel, Long> {
    long countByProjetoModel(ProjetoModel projetoModel);
    List<ItemModel> findByProjetoModelId(Long projetoId);
    List<ItemModel> findByUsuarioModelId(UUID usuarioId);
    
    @Query("""
    SELECT i.nivelAtividade, SUM(i.previsaoHoras)
    FROM ItemModel i
    WHERE i.projetoModel.id = :projetoId
    AND i.nivelAtividade IS NOT NULL
    AND i.previsaoHoras IS NOT NULL
    GROUP BY i.nivelAtividade
""")
    List<Object[]> somarHorasPorNivelAtividade(@Param("projetoId") Long projetoId);
}
