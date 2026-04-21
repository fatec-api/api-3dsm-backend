package br.com.jth.servico_gestao.repository;

import br.com.jth.servico_gestao.model.ItemModel;
import br.com.jth.servico_gestao.model.ProjetoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemModel, Long> {
    long countByProjetoModel(ProjetoModel projetoModel);
}
