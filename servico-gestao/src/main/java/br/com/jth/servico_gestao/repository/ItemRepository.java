package br.com.jth.servico_gestao.repository;

import br.com.jth.servico_gestao.model.ItemModel;
import br.com.jth.servico_gestao.model.ProjetoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<ItemModel, Long> {
    long countByProjetoModel(ProjetoModel projetoModel);
    List<ItemModel> findByProjetoModelId(Long projetoId);
    List<ItemModel> findByUsuarioModelId(UUID usuarioId);
}
