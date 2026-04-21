package br.com.jth.servico_gestao.repository;

import br.com.jth.servico_gestao.mapper.ProjetoMapper;
import br.com.jth.servico_gestao.model.ProjetoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetoRepository extends JpaRepository<ProjetoModel, Long> {
}
