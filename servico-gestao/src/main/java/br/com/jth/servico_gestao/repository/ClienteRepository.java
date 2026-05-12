package br.com.jth.servico_gestao.repository;

import br.com.jth.servico_gestao.model.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<ClienteModel, Long> {

    // validações de unicidade do email e cnpj do cliente
    Optional<ClienteModel> findByEmail(String email);
    Optional<ClienteModel> findByCnpj(String cnpj);
}