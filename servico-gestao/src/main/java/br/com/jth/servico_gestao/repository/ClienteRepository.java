package br.com.jth.servico_gestao.repository;

import br.com.jth.servico_gestao.model.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<ClienteModel, Long> {
}
